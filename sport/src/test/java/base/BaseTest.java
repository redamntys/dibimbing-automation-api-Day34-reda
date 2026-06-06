package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import utils.ConfigReader;

public class BaseTest {
    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");
    }
}