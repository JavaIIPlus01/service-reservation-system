package guru.bug.courses.srs.boundary.api.role;

import guru.bug.courses.srs.control.dao.RoleDAO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class RoleResourceTest {

    @Inject
    RoleDAO roleDAO;

    @Test
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void getRolesForAdmin() {
        roleDAO.createRole("get-all-roles-admin");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/roles")
                .then()
                .statusCode(200)
                .body("name", hasItem("get-all-roles-admin"))
                .body("id", notNullValue());
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void getRolesForUser() {
        roleDAO.createRole("get-all-roles-user");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/roles")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void createRoleForAdmin() {
        RoleDTO role = new RoleDTO();
        role.setName("create-new-service-admin");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(role)
                .when().post("/roles")
                .then()
                .statusCode(200)
                .body("name", is(role.getName()));
    }

    @Test
    void createRoleError() {
        RoleDTO role = new RoleDTO();
        role.setName("create-new-service-error");
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(role)
                .when().post("/roles")
                .then()
                .statusCode(401);
    }

}