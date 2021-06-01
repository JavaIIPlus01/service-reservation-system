package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.RoleEntity;
import guru.bug.courses.srs.entity.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserDAOTest {

    @Inject
    UserDAO userDAO;

    @Test
    void createUser() {
        assertDoesNotThrow(() -> userDAO.createUser("login", "salt1234".getBytes(), "password1234".getBytes()));
        assertThrows(Exception.class, () -> userDAO.createUser("login", "salt5678".getBytes(), "password5678".getBytes()));
    }

    @Test
    void findByLoginName() {
        userDAO.createUser("user1", "salt1".getBytes(), "password1".getBytes());
        userDAO.createUser("user2", "salt2".getBytes(), "password2".getBytes());

        var user1 = userDAO.findByLoginName("user1");
        assertTrue(user1.isPresent());
        assertEquals("user1", user1.get().getLoginName());

        var user2 = userDAO.findByLoginName("user2");
        assertTrue(user2.isPresent());
        assertEquals("user2", user2.get().getLoginName());

        var user3 = userDAO.findByLoginName("user3");
        assertFalse(user3.isPresent());
    }

    @Test
    void findAll() {
        userDAO.createUser("user1f", "salt1".getBytes(), "password1".getBytes());
        userDAO.createUser("user2f", "salt1".getBytes(), "password1".getBytes());
        userDAO.createUser("user3f", "salt1".getBytes(), "password1".getBytes());
        userDAO.createUser("user4f", "salt1".getBytes(), "password1".getBytes());

        var users = userDAO.findAll();
        assertThat(users, is(not(empty())));
        var userLogins = users.stream()
                .map(UserEntity::getLoginName)
                .collect(Collectors.toSet());
        assertThat(userLogins, hasItems("user1f", "user2f", "user3f", "user4f"));
    }
}