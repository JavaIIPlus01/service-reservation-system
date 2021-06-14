package guru.bug.courses.srs.boundary.api.token;

import guru.bug.courses.srs.control.UserControl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@QuarkusTest
class TokenResourceTest {

    @Inject
    UserControl userControl;

    @Test
    void whenDataCorrectProvideTokenTest() throws Exception {
        userControl.create("login1t", "salt1t", "password1t");
        Auth auth = new Auth();
        auth.setLogin("login1t");
        auth.setPassword("password1t");
        given()
                .body(auth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/tokens")
                .then()
                .statusCode(200)
                .body("token", startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9"))
                .body("expires", containsString(ZonedDateTime.now().plusMonths(3).format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    void whenCredentialsDontMatchFailTest() throws Exception {
        userControl.create("JustLogin", "simpleSalt", "notHashedPass");
        Auth auth = new Auth();
        auth.setLogin("JustLogin");
        auth.setPassword("notHashedPassAnother");
        given()
                .body(auth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/tokens")
                .then()
                .statusCode(401);
    }

    @Test
    void notValidBodyDataTest() {
        given()
                .body(new Auth())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/tokens")
                .then()
                .statusCode(400);
    }

    @Test
    void whenUserNotExistThenNoTokenTest() {
        Auth auth = new Auth();
        auth.setLogin("login2t");
        auth.setPassword("password2t");
        given()
                .body(auth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/tokens")
                .then()
                .statusCode(401);
    }

}