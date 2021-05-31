package guru.bug.courses.srs.boundary.api.token;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TestResourceTest {

    @Test
    void getTestHappyPath() {
        var userId = UUID.randomUUID();
        given()
                .pathParam("userId", userId)
                .queryParam("filter", "xyz")
                .queryParam("max-count", 20)
                .header("App-Version", "1.0")
                .header("Accept-Language", "ru")
                .when().get("/test/{userId}")
                .then()
                .statusCode(200)
                .body("userId", is(userId.toString()))
                .body("filter", is("xyz"));
    }

    @Test
    void getTestOutOfTheRange() {
        var userId = UUID.randomUUID();
        given()
                .pathParam("userId", userId)
                .queryParam("filter", "xyz")
                .queryParam("max-count", 5)
                .header("App-Version", "1.0")
                .header("Accept-Language", "ru")
                .when().get("/test/{userId}")
                .then()
                .statusCode(400);
    }
}