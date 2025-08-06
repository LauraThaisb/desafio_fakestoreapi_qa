package br.com.fakestore.api;

import br.com.fakestore.design.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CarrinhoTest extends BaseTest {

    private static String carrinho = "{\n" +
            "  \"userId\": 5,\n" +
            "  \"date\": \"2025-06-03\",\n" +
            "  \"products\": [\n" +
            "    {\n" +
            "      \"productId\": 19,\n" +
            "      \"quantity\": 2\n" +
            "    },\n" +
            "    {\n" +
            "      \"productId\": 20,\n" +
            "      \"quantity\": 1\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test(groups = { "health_check" })
    public void healthCheck() {
        when().
                get("/carts").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test(groups = { "contract" })
    public void contrato() {
        given().
                when().
                get("/carts").
                then().
                body(matchesJsonSchemaInClasspath("schemas/carts_schema.json"));
    }

    @Test(groups = { "functional" })
    public void cadastrarCarrinho() {

        Response retorno_carrinho = given().
                contentType(ContentType.JSON).
                body(carrinho).
                post("/carts").
                then().
                statusCode(HttpStatus.SC_CREATED).
                extract().
                response();

        Integer carrinhoId = retorno_carrinho.jsonPath().getInt("id");
        List<Integer> productIds = retorno_carrinho.jsonPath().getList("products.productId");
        List<Integer> quantities = retorno_carrinho.jsonPath().getList("products.quantity");

        assertThat(carrinhoId, notNullValue());
        assertThat(productIds, containsInAnyOrder(19, 20));
        assertThat(quantities, containsInAnyOrder(2, 1));
        assertThat(retorno_carrinho.jsonPath().getString("date"), equalTo("2025-06-03"));

    }

    @Test(groups = { "functional" })
    public void consultarCarrinho(int carrinhoId) {

        Response retornoConsulta = given().
                contentType(ContentType.JSON).
                when().
                get("/carts/" + carrinhoId).
                then().
                statusCode(HttpStatus.SC_OK).
                extract().
                response();

        Integer carrinhoConsultaId = retornoConsulta.jsonPath().getInt("id");
        List<Integer> consultaProdutosID = retornoConsulta.jsonPath().getList("products.productId");
        List<Integer> consultaQuantidades = retornoConsulta.jsonPath().getList("products.quantity");

        assertThat(carrinhoConsultaId, equalTo(carrinhoId));

    }

    /**
     * O endpoint retorna os dados, mas não deleta as informações
     * @param carrinhoId
     * @return
     */
    @Test(groups = { "functional" })
    public void removerCarrinho(int carrinhoId) {

        Response retornoRemocao = given().
                contentType(ContentType.JSON).
                when().
                delete("/carts/" + carrinhoId).
                then().
                statusCode(HttpStatus.SC_OK).
                extract().
                response();

        System.out.println(retornoRemocao.jsonPath().getString("date"));

    }

}