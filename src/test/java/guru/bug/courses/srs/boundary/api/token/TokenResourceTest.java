package guru.bug.courses.srs.boundary.api.token;

import guru.bug.courses.srs.control.PasswordHashEngine;
import guru.bug.courses.srs.control.dao.UserDAO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
class TokenResourceTest {

    @Inject
    UserDAO userDAO;

    @Inject
    PasswordHashEngine passwordHashEngine;

    @Test
    void whenDataCorrectProvideTokenTest() throws Exception {
        userDAO.createUser("login1t", "salt1t".getBytes(), passwordHashEngine.hash("password1t", "salt1t".getBytes()));
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
    void whenHashesDoNotMatchFailTest() {
        userDAO.createUser("JustLogin", "simpleSalt".getBytes(), "notHashedPass".getBytes());
        Auth auth = new Auth();
        auth.setLogin("JustLogin");
        auth.setPassword("notHashedPass");
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