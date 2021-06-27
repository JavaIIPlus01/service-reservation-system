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

import java.util.List;

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

    @Test
    void getServiceByIdForAdminTest() throws ServiceException {
        UserEntity savedUser = service.create("login-resource-5", "password-resource-5", "firstname-resource-5", "lastname-resource-5", "email-resource-5", "phone-resource-5");
        service.updateUser(savedUser.getId(), "login-resource-5", "firstname-resource-5", "lastname-resource-5", "phone-resource-5", "email-resource-5", List.of("admin"));
        UserEntity userToSearch = service.create("login-resource-6", "password-resource-6", "firstname-resource-6", "lastname-resource-6", "email-resource-6", "phone-resource-6");

        Auth auth = new Auth();
        auth.setLogin("login-resource-5");
        auth.setPassword("password-resource-5");
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
                .when().get("/users/" + userToSearch.getId().toString())
                .then()
                .statusCode(200)
                .body("loginName", is("login-resource-6"))
                .body("id", is(userToSearch.getId().toString()));
    }

    @Test
    void getServiceByIdUnauthorizedTest() throws ServiceException {
        service.create("login-resource-7", "password-resource-7", "firstname-resource-7", "lastname-resource-7", "email-resource-7", "phone-resource-7");
        UserEntity userToSearch = service.create("login-resource-8", "password-resource-8", "firstname-resource-8", "lastname-resource-8", "email-resource-8", "phone-resource-8");

        Auth auth = new Auth();
        auth.setLogin("login-resource-7");
        auth.setPassword("password-resource-7");
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
                .when().get("/users/" + userToSearch.getId().toString())
                .then()
                .statusCode(401);
    }





}