package restapi;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import org.hamcrest.Matchers;
import org.testng.annotations.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class Tests {
    final static String ROOT_URI = "http://localhost:7000/products";

    @BeforeTest
    public void resetDatabase() throws IOException {
        Files.copy(Paths.get("db_backup.json"), Paths.get("db.json"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    public void testGetProducts_success() {
        Response response = get(ROOT_URI + "/list");
        System.out.println(response.asString());
        response.then().body("id", hasItems(3, 4));
        response.then().body("name", hasItems("Smartwatch"));
    }
    @Test
    public void testPostProducts_success() {
        Response response = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\": \"Smartphone\",\"category\": \"Electronics\"}")
                .when()
                .post(ROOT_URI + "/create");
        System.out.println("POST Response\n" + response.asString());
// tests
        response.then().body("id", Matchers.any(Integer.class));
        response.then().body("name", Matchers.is("Smartphone"));
    }
    @Test
    public void testGetProductById_success() {
        int productId = 4;
        Response response = get(ROOT_URI + "/get/" + productId);

        System.out.println(response.toString());

        response.then().body("id", Matchers.is(4));
        response.then().body("name", Matchers.is("Headphones"));
    }
    @Test
    public void testPutProducts_success() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\": \"Smartphone\",\"category\": \"Electronics\"}")
                .when()
                .put(ROOT_URI + "/update/2");
        System.out.println("PUT Response\n" + response.asString());
        response.then().body("id", Matchers.is(2));
        response.then().body("name", Matchers.is("Smartphone"));
        response.then().body("category", Matchers.is("Electronics"));
    }
    @Test
    public void testDeleteProduct_success() {
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(ROOT_URI + "/list");

        int productId = getResponse.jsonPath().getInt("[0].id");
        System.out.println("Extracted productId: " + productId);

        Response response = delete(ROOT_URI + "/delete/" + productId);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());

        response.then().statusCode(200);

        response = get(ROOT_URI + "/list");
        System.out.println(response.asString());

        response.then().body("id", Matchers.not(productId));
    }


    @Test(dataProvider = "dpGetWithParam")
    public void get_with_param(int id, String name) {
        get(ROOT_URI + "/get/" + id).then().body("name", Matchers.is(name));
    }
    @DataProvider
    public Object[][] dpGetWithParam() {
        Object[][] testDatas = new Object[][]{
                new Object[]{2, "Smartphone"},
                new Object[]{3, "Coffee Maker"}};
        return testDatas;
    }
    @Test
    public void testUpdateProductAndValidate() {
        int productId = 2;

        Response getResponseBeforeUpdate = get(ROOT_URI + "/get/" + productId);
        getResponseBeforeUpdate.then().statusCode(200);

        String updatedProductJson = "{"
                + "\"name\": \"Updated Smartphone\","
                + "\"category\": \"Electronics\","
                + "\"price\": \"850 €\","
                + "\"stock\": 45"
                + "}";
        Response updateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updatedProductJson)
                .when()
                .put(ROOT_URI + "/update/" + productId);

        System.out.println("PUT Response: " + updateResponse.asString());

        updateResponse.then().statusCode(200);
        updateResponse.then().body("id", Matchers.is(productId));
        updateResponse.then().body("name", Matchers.is("Updated Smartphone"));
        updateResponse.then().body("category", Matchers.is("Electronics"));
        updateResponse.then().body("price", Matchers.is("850 €"));
        updateResponse.then().body("stock", Matchers.is(45));

        Response getResponseAfterUpdate = get(ROOT_URI + "/get/" + productId);
        System.out.println("GET Response after Update: " + getResponseAfterUpdate.asString());

        getResponseAfterUpdate.then().statusCode(200);
        getResponseAfterUpdate.then().body("id", Matchers.is(productId));
        getResponseAfterUpdate.then().body("name", Matchers.is("Updated Smartphone"));
        getResponseAfterUpdate.then().body("category", Matchers.is("Electronics"));
        getResponseAfterUpdate.then().body("price", Matchers.is("850 €"));
        getResponseAfterUpdate.then().body("stock", Matchers.is(45));

        getResponseAfterUpdate.then().body("price", Matchers.not("150 €"));
        getResponseAfterUpdate.then().body("stock", Matchers.not(30));
    }







}
