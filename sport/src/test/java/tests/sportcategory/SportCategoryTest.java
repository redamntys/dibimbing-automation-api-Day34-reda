package tests.sportcategory;

import base.BaseTest;
import body.sportcategory.SportCategoryBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TokenHelper;
import utils.Utils;

public class SportCategoryTest extends BaseTest {
    //Tambahkan Sportcategorytest
    //Utils (random data)
    //token helper (membantu mengambil token)
    private String categoryId;

    //Get Token -> ambil dari folder src/resources/json/token.json
    //Create
    @Test
    public void createSportCategories(){
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        String token = TokenHelper.getToken();
        String randomName = Utils.getCategoryName();

        //Ngehit endpoint
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .body(sportCategoryBody.createSportCategoryData("1231434").toString())
                .when()
                .post("v1/sport-categories/create")
                .then()
                .extract().response();

        System.out.println("Create Response: " + response.asString());

        //Assert

        //Get Category from response
        categoryId = response.jsonPath().getString("result.id");
        Assert.assertNotNull(categoryId,"Category ID should not be null");
        System.out.println("Created Category ID: " + categoryId);
    }
    //Read
    @Test
    public void getSportCategories(){
        String token = TokenHelper.getToken();
//curl --location 'https://sport-reservation-api-bootcamp.do.dibimbing.id/api/v1/sport-categories?is_paginate=false&per_page=&page=' \
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .queryParam("is_paginate","false")
                .queryParam("per_page","")
                .queryParam("page","")
                .when()
                .get("v1/sport-categories")
                .then()
                .extract().response();

        System.out.println("Get Response: " + response.asString());
    }
    //Update
    //Delete
    @Test
    public void deleteSportCategory(){
        String token = TokenHelper.getToken();

        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .when()
                .delete("v1/sport-categories/delete" + categoryId)
                .then()
                .extract().response();

        System.out.println("Get Response: " + response.asString());
    }
    //E2E
}
