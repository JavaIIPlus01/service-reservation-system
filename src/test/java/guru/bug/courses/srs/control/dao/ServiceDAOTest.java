package guru.bug.courses.srs.control.dao;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ServiceDAOTest {

    @Inject
    ServiceDAO serviceDAO;

    @Test
    void createAndFindService() {
        assertDoesNotThrow(() -> serviceDAO.createService("create-service-min", 10));
        var all = serviceDAO.findAll();
        assertThat(all, is(not(empty())));

        var min = all.stream()
                .filter(s -> "create-service-min".equals(s.getName())).findFirst();
        assertTrue(min.isPresent());
        assertEquals(10, min.get().getDefaultDuration());
        assertNull(min.get().getDescription());
    }

}