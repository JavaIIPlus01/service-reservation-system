package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.ServiceDAO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ServiceControlTest {

    @Inject
    ServiceControl serviceControl;

    @Inject
    ServiceDAO serviceDAO;

    @Test
    void findAndUpdateService() {
        var service = serviceDAO.createService("service-control", "control-description",10);

        var service1 = serviceControl.updateService(service.getId(), "update-control", "update-description", 20);
        assertTrue(service1.isPresent());
        assertEquals("update-control", service1.get().getName());
        assertEquals("update-description", service1.get().getDescription());
        assertEquals(20, service1.get().getDefaultDuration());

        var service2 = serviceControl.updateService(UUID.randomUUID(), "update-control-2", "update-description-2", 20);
        assertFalse(service2.isPresent());
    }
}