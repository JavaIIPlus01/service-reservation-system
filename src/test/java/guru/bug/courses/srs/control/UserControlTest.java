package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.RoleDAO;
import guru.bug.courses.srs.control.dao.UserDAO;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.entity.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserControlTest {

    @Inject
    UserDAO userDAO;

    @Inject
    RoleDAO roleDAO;

    @Inject
    PasswordHashEngine passwordHashEngine;

    @Inject
    UserControl userControl;

    @Test
    void updateUser() throws ServiceException {
        UserEntity savedUser = userDAO.createUser("login-control-1", "password1234".getBytes(), "salt1234".getBytes(), "FirstName-control-1", "LastName-control-1", "Email-control-1", "Phone-control-1");
        UserEntity updatedUser =  userControl.updateUser(savedUser.getId(), "login-control-2", "FirstName-control-2", "LastName-control-2", "Phone-control-2","Email-control-2",  List.of("admin"), "newPassword-control-2").orElseThrow();
        assertEquals(roleDAO.findAll().stream().filter(role -> role.getName().equals("admin")).collect(Collectors.toSet()), updatedUser.getRoles());
        assertEquals("login-control-2", updatedUser.getLoginName());
        assertEquals("FirstName-control-2", updatedUser.getFirstName());
        assertEquals("LastName-control-2", updatedUser.getLastName());
        assertEquals("Email-control-2", updatedUser.getEmail());
        assertEquals("Phone-control-2", updatedUser.getPhone());
        assertArrayEquals(passwordHashEngine.hash("newPassword-control-2", userDAO.findByLoginName("login-control-2").orElseThrow().getSalt()), updatedUser.getPasswordHash());
    }

    @Test
    void updateUserWithoutPassword() {
        UserEntity savedUser = userDAO.createUser("login-control-12", "password-control-12".getBytes(), "salt1234".getBytes(), "FirstName-control-12", "LastName-control-12", "Email-control-12", "Phone-control-12");
        UserEntity updatedUser =  userControl.updateUser(savedUser.getId(), "login-control-5", "FirstName-control-5", "LastName-control-5", "Phone-control-5","Email-control-5",  List.of("admin")).orElseThrow();
        assertEquals(roleDAO.findAll().stream().filter(role -> role.getName().equals("admin")).collect(Collectors.toSet()), updatedUser.getRoles());
        assertEquals("login-control-5", updatedUser.getLoginName());
        assertEquals("FirstName-control-5", updatedUser.getFirstName());
        assertEquals("LastName-control-5", updatedUser.getLastName());
        assertEquals("Email-control-5", updatedUser.getEmail());
        assertEquals("Phone-control-5", updatedUser.getPhone());
        assertArrayEquals(savedUser.getPasswordHash(), updatedUser.getPasswordHash());
    }

    @Test
    void isAdmin() throws ServiceException {
       UserEntity user = userDAO.createUser("login-control-3", "password1234".getBytes(), "salt1234".getBytes(), "FirstName-control-3", "LastName-control-3", "Email-control-3", "Phone-control-3");
       assertFalse(userControl.isAdmin(user.getId()));
       assertFalse(userControl.isAdmin(UUID.randomUUID()));
       userControl.updateUser(user.getId(), "login-control-4", "FirstName-control-4", "LastName-control-4", "Phone-control-4","Email-control-4",  List.of("admin"), "newPassword-control-4");
       assertTrue(userControl.isAdmin(user.getId()));
    }

}