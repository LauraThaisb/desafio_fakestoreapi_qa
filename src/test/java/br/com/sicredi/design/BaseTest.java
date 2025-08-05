package br.com.sicredi.design;

import br.com.sicredi.configuration.Configuration;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

public abstract class BaseTest {

    @BeforeClass(alwaysRun = true)
    public static void beforeClass() {
        Configuration configuration = new Configuration();

        baseURI = configuration.getBaseURI();

    }

}
