import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MongoDbRestApiTester {
    String port = "8090";
    String lastId = "";

    @Test
    void testGetAll() {
        Response response = get("http://localhost:" + port + "/api/reviews");
        System.out.println(response.asString());

        int statusCode = response.getStatusCode();

        // Assert
        Assert.assertEquals(statusCode, 204);
        System.out.println("Get all passed");
    }

    @Test
    void testPort() {
        // post
        String requestBody = "{\n" +
                "  \"stars\": 5,\n" +
                "  \"productDescription\": \"Taco\",\n" +
                "  \"reviewComments\": \"Great tacos!\",\n" +
                "  \"contactPhone\": \"123-456-7890\",\n" +
                "  \"contactEmail\": \"test@test.com\",\n" +
                "  \"actionNeeded\": false\n}";
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("http://localhost:" + port + "/api/reviews")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.getStatusCode(), 201); // created
        lastId = response.jsonPath().getString("id");
        System.out.println("last ID after port: " + lastId);
        System.out.println("Post passed");
    }

    @Test
    void testPut() {
        // put
        String requestBody = "{\n" +
                "  \"stars\": 5,\n" +
                "  \"productDescription\": \"Taco\",\n" +
                "  \"reviewComments\": \"Great tacos!\",\n" +
                "  \"contactPhone\": \"123-456-7890\",\n" +
                "  \"contactEmail\": \"test@test.com\",\n" +
                "  \"actionNeeded\": true\n}";
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("http://localhost:" + port + "/api/reviews/" + lastId)
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.getStatusCode(), 200); // ok
        lastId = response.jsonPath().getString("id");
        System.out.println("last ID after put: " + lastId);
        System.out.println("Put passed");
    }

    @Test
    void testDeleteById() {
        // delete
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete("http://localhost:" + port + "/api/reviews/" + lastId)
                .then()
                .extract()
                .response();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode == 200 || statusCode == 204, true); // ok or no content
        System.out.println("Delete by id passed");
    }
}
