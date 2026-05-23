package tests.auth;

import body.auth.LoginBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginTest {
    @Test
    public void loginTest() throws IOException {
        //Hit endpoint url auth
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");

        //generate payloadlogin
        //ambil payload dari method di src/main/java/body/auth
        LoginBody loginBody = new LoginBody();

        //Hit endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody.loginData().toString())
                .when()
                .post("v1/login")
                .then()
                .extract().response();

        System.out.println("Response : " + response.asString());

        //extract token
        String token = response.jsonPath().get("data.token");
        System.out.println("Token : " + token);

        //Simpan token di resource token.json
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", token);

        try (FileWriter file = new FileWriter("src/resources/json/token.json")){
            file.write(tokenJson.toJSONString());
            file.flush();
        }
        System.out.println("Token saved to token.json");
    }
}
