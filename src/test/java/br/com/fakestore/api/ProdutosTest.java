package br.com.fakestore.api;

import br.com.fakestore.design.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ProdutosTest extends BaseTest {

    private static String produto_1 = "{\n" +
            "  \"title\": \"Tenis Corrida\",\n" +
            "  \"price\": \"199.90\",\n" +
            "  \"description\": \"Tenis leve para esportes\",\n" +
            "  \"image\": \"https://i.pravatar.cc\",\n" +
            "  \"category\": \"shoes\"\n" +
            "}";

    private static String produto_2 = "{\n" +
            "  \"title\": \"Relogio Digital\",\n" +
            "  \"price\": \"149.50\",\n" +
            "  \"description\": \"Relogio à prova d'agua\",\n" +
            "  \"image\": \"https://i.pravatar.cc\",\n" +
            "  \"category\": \"accessories\"\n" +
            "}";


    @Test(groups = { "health_check" })
    public void healthCheck() {
        when().
                get("/products").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test(groups = { "contract" })
    public void contrato() {
        given().
                when().
                get("/products").
                then().
                body(matchesJsonSchemaInClasspath("schemas/products_schema.json"));
    }

    @Test(groups = { "functional" })
    public void cadastrarProdutos() {

        Response retorno_produto1 = given().
                contentType(ContentType.JSON).
                body(produto_1).
                post("/products").
                then().
                statusCode(HttpStatus.SC_CREATED).
                extract().
                response();

        Response retorno_produto2 = given().
                contentType(ContentType.JSON).
                body(produto_2).
                post("/products").
                then().
                statusCode(HttpStatus.SC_CREATED).
                extract().
                response();

        Integer generatedId1 = retorno_produto1.jsonPath().getInt("id");
        assertThat(generatedId1, notNullValue());

        Integer generatedId2 = retorno_produto2.jsonPath().getInt("id");
        assertThat(generatedId2, notNullValue());

    }

    @Test(groups = { "functional" })
    public void consultarProdutos() {
        String listaProdutos = given().
                contentType(ContentType.JSON).
                get("/products/20").
                then().
                statusCode(HttpStatus.SC_OK).
                extract().
                response().
                asString();

        assertThat(listaProdutos, notNullValue());

    }

}