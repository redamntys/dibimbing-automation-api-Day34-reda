package tests.sportcategory;

import base.BaseTest;
import body.sportcategory.SportCategoryBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.TokenHelper;
import utils.Utils;
import org.json.simple.JSONObject;

public class SportCategoryTest extends BaseTest {
    private String categoryId;

    @BeforeClass
    public void before() {
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        String token = TokenHelper.getToken();
        //Ngehit endpoint
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .body(sportCategoryBody.createSportCategoryData(generateRandomName()).toString())
                .when()
                .post("v1/sport-categories/create")
                .then()
                .extract().response();

        categoryId = response.jsonPath().getString("result.id");
    }

    //Create
    //curl --location 'https://sport-reservation-api-bootcamp.do.dibimbing.id/api/v1/sport-categories/create
    @Test(description = "Verify successful creation of sport category")
    public void createSportCategories(){
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        String token = TokenHelper.getToken();

        //Ngehit endpoint
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .body(sportCategoryBody.createSportCategoryData(generateRandomName()).toString())
                .when()
                .post("v1/sport-categories/create")
                .then()
                .extract().response();

        System.out.println("Create Response: " + response.asString());

        //Get Category from response
        String categoryId = response.jsonPath().getString("result.id");
        //Assert
        Assert.assertNotNull(categoryId,"Category ID should not be null");
        Assert.assertNotNull(response, "Got null as response");
        Assert.assertFalse(Boolean.parseBoolean(response.jsonPath().getString("error")));
        Assert.assertEquals(response.jsonPath().getString("message"), "data saved");
    }

    //Update
    //curl --location 'https://sport-reservation-api-bootcamp.do.dibimbing.id/api/v1/sport-categories/create
    @Test (description = "Verify successful update of sport category")
    public void updateSportCategory() {
        String token = TokenHelper.getToken();
        Assert.assertNotNull(categoryId, "Category ID that will be updated should not be null");

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", generateRandomName());

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("v1/sport-categories/update/" + categoryId)
                .then()
                .extract()
                .response();

        System.out.println("Update Response: " + response.asString());

        //Assert
        Assert.assertNotNull(response);
        Assert.assertFalse(Boolean.parseBoolean(response.jsonPath().getString("error")));
        Assert.assertEquals(response.jsonPath().getString("message"), "data saved");
    }

    //Get
    //curl --location 'https://sport-reservation-api-bootcamp.do.dibimbing.id/api/v1/sport-categories?is_paginate=false&per_page=&page=' \
    @Test (description = "Verify successful retrieval of sport categories")
    public void getSportCategories(){
        String token = TokenHelper.getToken();

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

        //Assert
       Assert.assertNotNull(response);
       Assert.assertFalse(Boolean.parseBoolean(response.jsonPath().getString("error")));
       Assert.assertNotEquals(response.jsonPath().getString("result").length(), 0);
    }

    //Delete
    //curl --location 'https://sport-reservation-api-bootcamp.do.dibimbing.id/api/v1/sport-categories/delete/{{category_id}}
    @Test(description = "Verify successful deletion of sport category", dependsOnMethods = "updateSportCategory", alwaysRun = true)
    public void deleteSportCategory(){
        String token = TokenHelper.getToken();
        Assert.assertNotNull(categoryId, "Category ID that will be deleted should not be null");

        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type", "application/json")
                .when()
                .delete("v1/sport-categories/delete/" + categoryId)
                .then()
                .extract().response();

        System.out.println("Delete Response: " + response.asString());

        //Assert
        Assert.assertNotNull(response);
        Assert.assertFalse(Boolean.parseBoolean(response.jsonPath().getString("error")));
        Assert.assertEquals(response.jsonPath().getString("message"), "Data deleted successfully");
    }

    //Utils (random data)
    private String generateRandomName() {
        return Utils.getCategoryName();
    }
}
