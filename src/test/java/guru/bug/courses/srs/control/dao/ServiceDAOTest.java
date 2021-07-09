package guru.bug.courses.srs.control.dao;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ServiceDAOTest {

    @Inject
    ServiceDAO serviceDAO;

    @Test
    void createAndFindAll() {
        assertDoesNotThrow(() -> serviceDAO.createService("create-service", "create-description",10));
        var all = serviceDAO.findAll();
        assertThat(all, is(not(empty())));

        var min = all.stream()
                .filter(s -> "create-service".equals(s.getName())).findFirst();
        assertTrue(min.isPresent());
        assertEquals("create-description", min.get().getDescription());
        assertEquals(10, min.get().getDefaultDuration());
        //assertNull(min.get().getDescription());
    }

    @Test
    void findByIdAndUpdate() {
        var service = serviceDAO.createService("find-service", "find-description", 20);

        var service1 = serviceDAO.findById(service.getId());
        assertTrue(service1.isPresent());
        assertEquals("find-service", service1.get().getName());
        assertEquals("find-description", service1.get().getDescription());
        assertEquals(20, service1.get().getDefaultDuration());

        var service2 = serviceDAO.findById(UUID.randomUUID());
        assertFalse(service2.isPresent());

        serviceDAO.updateService(service, "update-service", "update-description", 30);
        assertEquals("update-service", service.getName());
        assertEquals("update-description", service.getDescription());
        assertEquals(30, service.getDefaultDuration());
    }
}