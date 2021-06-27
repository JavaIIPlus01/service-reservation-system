package guru.bug.courses.srs.boundary.api.user;

import guru.bug.courses.srs.boundary.api.token.Auth;
import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.control.token.TokenData;
import guru.bug.courses.srs.entity.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class UserResourceTest {

    @Inject
    UserControl service;

    @Test
    void registerSuccessTest() {
        User user = new User();
        user.setFirstName("Firstname-resource-1");
        user.setLastName("Lastname-resource-1");
        user.setEmail("Email-resource-1");
        user.setPhone("Phone-resource-1");
        user.setLoginName("Login-resource-1");
        user.setPassword("Password-resource-1");
        given()
                .body(user)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/users")
                .then()
                .statusCode(200)
                .body("id", is(not(nullValue())));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void getUsersSuccessTest() throws ServiceException {
        service.create("login-resource-3", "password-resource-3", "firstname-resource-3", "lastname-resource-3", "email-resource-3", "phone-resource-3");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("loginName", hasItem("login-resource-3"))
                .body("id", notNullValue());
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void getUsersForbiddenTest() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/users")
                .then()
                .statusCode(403);
    }

    @Test
    void getServiceByIdTest() throws ServiceException {
        UserEntity savedUser = service.create("login-resource-4", "password-resource-4", "firstname-resource-4", "lastname-resource-4", "email-resource-4", "phone-resource-4");
        Auth auth = new Auth();
        auth.setLogin("login-resource-4");
        auth.setPassword("password-resource-4");
        var token = given()
                .body(auth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/tokens")
                .then()
                .statusCode(200)
                .extract().body().as(TokenData.class);

        given()
                .accept(JSON)
                .contentType(JSON)
                .header(new Header("Authorization", "Bearer " + token.getToken()))
                .when().get("/users/" + savedUser.getId().toString())
                .then()
                .statusCode(200)
                .body("loginName", is("login-resource-4"))
                .body("id", is(savedUser.getId().toString()));

    }

}