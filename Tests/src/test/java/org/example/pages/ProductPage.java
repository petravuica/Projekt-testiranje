package org.example.pages;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;


public class ProductPage {
    private static final String ROOT_URI = "http://localhost:7000/products";

    public Response getProductList() {
        return get(ROOT_URI + "/list");
    }

    public Response getProductById(int productId) {
        return get(ROOT_URI + "/get/" + productId);
    }

    public Response createProduct(String productJson) {
        return given()
                .contentType("application/json")
                .body(productJson)
                .when()
                .post(ROOT_URI + "/create");
    }

    public Response updateProduct(int productId, String updatedProductJson) {
        return given()
                .contentType("application/json")
                .body(updatedProductJson)
                .when()
                .put(ROOT_URI + "/update/" + productId);
    }

    public Response deleteProduct(int productId) {
        return delete(ROOT_URI + "/delete/" + productId);
    }
}
