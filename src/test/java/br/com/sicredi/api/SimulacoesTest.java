package br.com.sicredi.api;

import br.com.sicredi.design.BaseTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class SimulacoesTest extends BaseTest {

    private static final String CPF_SEM_SIMULACAO = "62862138053";
    private static final String CPF_COM_SIMULACAO = "17822386034";

    private static String simulacao_valida = "{\n" +
            "  \"nome\": \"Laura Thais\",\n" +
            "  \"cpf\": \"42485688028\",\n" +
            "  \"email\": \"teste@sicredi.teste\"\n" +
            "  \"valor\": \"12000\"\n" +
            "  \"parcelas\": \"3\"\n" +
            "  \"seguro\": \"true\"\n" +
            "}";

    private static String simulacao_existente = "{\n" +
            "  \"nome\": \"Laura Thais\",\n" +
            "  \"cpf\": \"17822386034\",\n" +
            "  \"email\": \"teste@sicredi.teste\"\n" +
            "  \"valor\": \"12000\"\n" +
            "  \"parcelas\": \"3\"\n" +
            "  \"seguro\": \"true\"\n" +
            "}";

    private static String simulacao_alterada = "{\n" +
            "  \"nome\": \"Laura Alteracao\",\n" +
            "  \"cpf\": \"17822386034\",\n" +
            "  \"email\": \"teste@sicredi.teste\"\n" +
            "  \"valor\": \"12000\"\n" +
            "  \"parcelas\": \"3\"\n" +
            "  \"seguro\": \"true\"\n" +
            "}";

    @Test(groups = { "health_check" })
    public void healthCheck() {
        when().
                get("/api/v1/simulacoes").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test(groups = { "contract" })
    public void contrato() {
        given().
                pathParam("cpf", CPF_COM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                body(matchesJsonSchemaInClasspath("schemas/simulacoes_schema.json"));
    }

    @Test(groups = { "functional" })
    public void consultarTodasSimulacoes() {
        when().
                get("/api/v1/simulacoes").
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("size()", is(2));

    }

    @Test(groups = { "functional" })
    public void consultarSimulacaoExistente() {
        given().
                pathParam("cpf", CPF_COM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_OK).
                body(
                        "nome", equalTo("Deltrano"),
                        "cpf", equalTo("17822386034"),
                        "email", equalTo("deltrano@gmail.com"),
                        "parcelas", equalTo(5),
                        "seguro", equalTo(false)
                );
    }

    @Test(groups = { "functional" })
    public void consultarSimulacaoNaoExistente() {
        given().
                pathParam("cpf", CPF_SEM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = { "functional" })
    public void criarSimulacaoValida() {
        given().
                contentType(ContentType.JSON).
                body(simulacao_valida).
                post("/api/v1/simulacoes").
                then().
                statusCode(HttpStatus.SC_CREATED).
                extract().
                response();
    }

    @Test(groups = { "functional" })
    public void criarSimulacaoExistente() {
        /**Ao criar uma simulação existente, apresenta a mensagem
         * "CPF duplicado"
         * E o status code 400
         * */
        given().
                contentType(ContentType.JSON).
                body(simulacao_existente).
                post("/api/v1/simulacoes").
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST).
                extract().
                response();
    }

    @Test(groups = { "functional" })
    public void alterarSimulacaoExistente() {
        given().
                pathParam("cpf", CPF_COM_SIMULACAO).
                contentType(ContentType.JSON).
                body(simulacao_alterada).
                put("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_OK).
                extract().
                response();
    }

    @Test(groups = { "functional" })
    public void alterarSimulacaoNaoExistente() {
        given().
                pathParam("cpf", CPF_SEM_SIMULACAO).
                contentType(ContentType.JSON).
                body(simulacao_alterada).
                put("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_OK).
                extract().
                response();
    }

    @Test(groups = { "functional" })
    public void removerSimulacaoExistente() {
        given().
                pathParam("id", 11).
                when().
                delete("/api/v1/simulacoes/{id}").
                then().
                statusCode(HttpStatus.SC_OK);
    }

}