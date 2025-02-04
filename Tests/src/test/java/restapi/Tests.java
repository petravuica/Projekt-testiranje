package restapi;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
public class Tests {
    final static String ROOT_URI = "http://localhost:7000/products";

    @Test
    public void testGetProducts_success() {
        Response response = get(ROOT_URI + "/list");
        System.out.println(response.asString());
        response.then().body("id", hasItems(3, 4));
        response.then().body("name", hasItems("Laptop"));
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
    public void testGetPlanetById_success() {
        int productId = 4;  // ID of the planet to retrieve
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
    public void testDeletePlanet_success() {
        // Retrieve the list of planets
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(ROOT_URI + "/list");

        // Extract the planet ID from the response
        int productId = getResponse.jsonPath().getInt("[0].id");
        System.out.println("Extracted productId: " + productId);

        // Delete the planet using the extracted `planetId`
        Response response = delete(ROOT_URI + "/delete/" + productId);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());

        // Verify that the status code is 200 indicating success
        response.then().statusCode(200);

        // Retrieve the list of planets again
        response = get(ROOT_URI + "/list");
        System.out.println(response.asString());

        // Verify that the response does not contain the deleted planet
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
        // Kreiramo novi proizvod za testiranje
        int productId = 2;  // ID proizvoda koji želimo da ažuriramo

        // Prvo proveravamo da proizvod postoji pre nego što ga ažuriramo
        Response getResponseBeforeUpdate = get(ROOT_URI + "/get/" + productId);
        getResponseBeforeUpdate.then().statusCode(200); // Proveravamo da proizvod postoji

        // Ažuriramo proizvod sa novim podacima
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

        // Proveravamo da li je proizvod ažuriran (status kod 200 i ID i ime)
        updateResponse.then().statusCode(200);
        updateResponse.then().body("id", Matchers.is(productId));
        updateResponse.then().body("name", Matchers.is("Updated Smartphone"));
        updateResponse.then().body("category", Matchers.is("Electronics"));
        updateResponse.then().body("price", Matchers.is("850 €"));
        updateResponse.then().body("stock", Matchers.is(45));

        // Sada proveravamo da li su svi podaci o proizvodu ažurirani kroz GET zahtev
        Response getResponseAfterUpdate = get(ROOT_URI + "/get/" + productId);
        System.out.println("GET Response after Update: " + getResponseAfterUpdate.asString());

        // Verifikacija da su svi podaci tačni nakon ažuriranja
        getResponseAfterUpdate.then().statusCode(200);
        getResponseAfterUpdate.then().body("id", Matchers.is(productId));
        getResponseAfterUpdate.then().body("name", Matchers.is("Updated Smartphone"));
        getResponseAfterUpdate.then().body("category", Matchers.is("Electronics"));
        getResponseAfterUpdate.then().body("price", Matchers.is("850 €"));
        getResponseAfterUpdate.then().body("stock", Matchers.is(45));

        // Verifikacija da proizvod nije promenio u nekom drugom aspektu
        getResponseAfterUpdate.then().body("price", Matchers.not("150 €"));
        getResponseAfterUpdate.then().body("stock", Matchers.not(30));
    }







}
