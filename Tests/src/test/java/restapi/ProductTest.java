package restapi;

import org.example.pages.ProductPage;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.hamcrest.Matchers;

public class ProductTest {

    ProductPage productPage = new ProductPage(); // Page Object instance

    @Test
    public void testGetProducts() {
        Response response = productPage.getProductList();
        response.then().body("id", Matchers.hasItems(2, 3));
    }

    @Test
    public void testCreateProduct() {
        String productJson = "{\"name\": \"Smartwatch\", \"category\": \"Electronics\"}";
        Response response = productPage.createProduct(productJson);
        response.then().body("name", Matchers.is("Smartwatch"));
    }
}
