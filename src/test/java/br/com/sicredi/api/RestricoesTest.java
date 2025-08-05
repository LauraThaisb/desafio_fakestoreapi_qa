package br.com.sicredi.api;

import br.com.sicredi.design.BaseTest;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class RestricoesTest extends BaseTest {

    private static final String CPF_SEM_RESTRICAO = "62862138053";
    private static final String CPF_COM_RESTRICAO = "97093236014";

    @Test(groups = { "health_check" })
    public void healthCheck() {
        when().
                get("/api/v1/restricoes/{cpf}", CPF_SEM_RESTRICAO).
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test(groups = { "contract" })
    public void contrato() {
        given().
                pathParam("cpf", CPF_COM_RESTRICAO).
                when().
                get("/api/v1/restricoes/{cpf}").
                then().
                body(matchesJsonSchemaInClasspath("schemas/restricoes_schema.json"));
    }

    @Test(groups = { "functional" })
    public void pessoaComRestricao() {
        given().
                pathParam("cpf", CPF_COM_RESTRICAO).
                when().
                get("/api/v1/restricoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_OK).
                body("mensagem", is("O CPF 97093236014 tem problema"));
    }

    @Test(groups = { "functional" })
    public void pessoaSemRestricao() {
        given().
                pathParam("cpf", CPF_SEM_RESTRICAO).
                when().
                get("/api/v1/restricoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

}