package guru.bug.courses.srs.control.dao;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RoleDAOTest {

    @Inject
    RoleDAO roleDAO;

    @Test
    void createRole() {
        roleDAO.createRole("createRoleTest");
        assertThrows(Exception.class, () -> {
            roleDAO.createRole("createRoleTest");
        });
    }
}