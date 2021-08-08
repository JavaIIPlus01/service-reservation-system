package guru.bug.courses.srs.boundary.api.service;

import guru.bug.courses.srs.control.dao.ServiceDAO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class ServiceResourceTest {

    @Inject
    ServiceDAO serviceDAO;

    @Test
    void getServices() {
        serviceDAO.createService("get-all-services", "all-services",20);
        given()
                .accept(MediaType.APPLICATION_JSON)
                .when().get("/services")
                .then()
                .statusCode(200)
                .body("name", hasItem("get-all-services"))
                .body("id", notNullValue());
    }

    @Test
    void getServiceById() {
        String serviceId = serviceDAO.createService("get-service-by-id", "service-by-id",30).getId().toString();
        given()
                .accept(MediaType.APPLICATION_JSON)
                .pathParam("serviceId", serviceId)
                .when().get("/services/{serviceId}")
                .then()
                .statusCode(200)
                .body("id", is(serviceId));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void createServiceOk() {
        ServiceDTO service = new ServiceDTO();
        service.setName("create-new-service-ok");
        service.setDescription("new-service-ok");
        service.setDefaultDuration(10);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service)
                .when().post("/services")
                .then()
                .statusCode(200)
                .body("name", is(service.getName()));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void createServiceForbidden() {
        ServiceDTO service = new ServiceDTO();
        service.setName("create-new-service-error");
        service.setDescription("new-service-error");
        service.setDefaultDuration(20);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service)
                .when().post("/services")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"admin", "user"})
    void updateServiceOk() {
        String serviceId = serviceDAO.createService("update-service-ok", "update-ok",30).getId().toString();
        ServiceDTO service = new ServiceDTO();
        service.setName("new-name");
        service.setDescription("new-description");
        service.setDefaultDuration(40);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("serviceId", serviceId)
                .body(service)
                .when().put("/services/{serviceId}")
                .then()
                .statusCode(200)
                .body("description", is(service.getDescription()));
    }

    @Test
    void updateServiceForbidden() {
        String serviceId = serviceDAO.createService("update-service-error", "update-error",40).getId().toString();
        ServiceDTO service = new ServiceDTO();
        service.setName("new-name");
        service.setDescription("new-description");
        service.setDefaultDuration(40);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("serviceId", serviceId)
                .body(service)
                .when().put("/services/{serviceId}")
                .then()
                .statusCode(401);
    }
}