package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.RoleEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RoleDAOTest {

    @Inject
    RoleDAO roleDAO;

    @Test
    void createRole() {
        assertDoesNotThrow(() -> roleDAO.createRole("create-role"));
        assertThrows(Exception.class, () -> roleDAO.createRole("create-role"));
    }

    @Test
    void findRoleByName() {
        roleDAO.createRole("find-by-name-1");
        roleDAO.createRole("find-by-name-2");

        var role1 = roleDAO.findRoleByName("find-by-name-1");
        assertTrue(role1.isPresent());
        assertEquals("find-by-name-1", role1.get().getName());

        var role2 = roleDAO.findRoleByName("find-by-name-2");
        assertTrue(role2.isPresent());
        assertEquals("find-by-name-2", role2.get().getName());

        var role3 = roleDAO.findRoleByName("find-by-name-3");
        assertFalse(role3.isPresent());

    }

    @Test
    void findAll() {
        roleDAO.createRole("find-all-1");
        roleDAO.createRole("find-all-2");
        roleDAO.createRole("find-all-3");
        roleDAO.createRole("find-all-4");

        var roles = roleDAO.findAll();
        assertThat(roles, is(not(empty())));
        var roleNames = roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
        assertThat(roleNames, hasItems("find-all-1", "find-all-2", "find-all-3", "find-all-4"));
    }
}