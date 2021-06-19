package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.control.dao.ServiceDAO;
import guru.bug.courses.srs.entity.ServiceEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ServiceResourceTest {

    @Inject
    ServiceDAO serviceDAO;

    @Test
    void getServices() {
        serviceDAO.createService("get-all-services", "all-services",10);

        var resp = given()
                .accept(MediaType.APPLICATION_JSON)
                .when().get("/services")
                .then()
                .statusCode(200)
                .extract().body().asPrettyString();
        System.out.println(resp);
    }

    @Test
    void getServiceById() {
        var service = serviceDAO.createService("get-service-id", "service-id",20);
        var serviceId = service.getId().toString();

        given()
                .accept(MediaType.APPLICATION_JSON)
                .pathParam("serviceId", serviceId)
                .when().get("/services/{serviceId}")
                .then()
                .statusCode(200)
                .body("id", is(serviceId));
    }

    @Test
    void createServiceError() {
        ServiceEntity service = new ServiceEntity();
        service.setName("create-new-service");
        service.setDescription("new-service");
        service.setDefaultDuration(20);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service)
                .when().post("/services")
                .then()
                .statusCode(401);
                //.body("name", is(service.getName()));
    }
}