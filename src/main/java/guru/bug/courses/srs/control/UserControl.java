package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.RoleDAO;
import guru.bug.courses.srs.control.dao.UserDAO;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.entity.RoleEntity;
import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);
    private static final String ADMIN = "admin";

    @Inject
    UserDAO userDAO;

    @Inject
    RoleDAO roleDAO;

    @Inject
    PasswordHashEngine passwordHashEngine;

    public Optional<UserEntity> searchByLogin(String login) {
        LOG.debug("Searching for user by login {}", login);
        return userDAO.findByLoginName(login);
    }

    public UserEntity create(String login, String salt, String password) throws ServiceException {
        return userDAO.createUser(login, salt.getBytes(), passwordHashEngine.hash(password, salt.getBytes()));
    }

    public UserEntity create(String login, String password, String firstName, String lastName, String email, String phone) throws ServiceException {
        String salt = UUID.randomUUID().toString().replace("-", "");
        return userDAO.createUser(login, passwordHashEngine.hash(password, salt.getBytes()), salt.getBytes(), firstName, lastName, email, phone);
    }

    public Collection<UserEntity> getAll() {
        var result = userDAO.findAll();
        LOG.debug("Selected list of {} users", result.size());
        return result;
    }

    public Optional<UserEntity> findById(UUID id) {
        var user = userDAO.findById(id);
        user.ifPresentOrElse(
                u -> LOG.debug("User {} found {}", id, u),
                () -> LOG.debug("User {} not found", id));
        return user;
    }

    public boolean isAdmin(UUID claimId) {
        Optional<UserEntity> userById = userDAO.findById(claimId);
        return userById.stream()
                .anyMatch(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(ADMIN)));
    }

    public Optional<UserEntity> updateUser(UUID id, String login, String firstName, String lastName, String phone,
                                           String email, List<String> roles, String password) throws ServiceException {
        var result = findById(id);
        String salt = UUID.randomUUID().toString().replace("-", "");
        byte [] pwdHash = passwordHashEngine.hash(password, salt.getBytes());
        result.ifPresentOrElse(
                u -> userDAO.updateUser(u, login, firstName, lastName, email, phone, Objects.isNull(roles) ? u.getRoles() : convertRoles(roles), pwdHash),
                () -> LOG.debug("Service {} not exist", id));
        return result;
    }

    public Optional<UserEntity> updateUser(UUID id, String login, String firstName, String lastName, String phone,
                                           String email, List<String> roles) {
        var result = findById(id);
        result.ifPresentOrElse(
                u -> userDAO.updateUser(u, login, firstName, lastName, email, phone, Objects.isNull(roles) ? u.getRoles() : convertRoles(roles), u.getPasswordHash()),
                () -> LOG.debug("Service {} not exist", id));
        return result;
    }

    private Set<RoleEntity> convertRoles(List<String> roles) {
        return roleDAO.findAll().stream()
                .filter(role -> roles.stream()
                        .anyMatch(r -> role.getName().equals(r)))
                .collect(Collectors.toSet());
    }
}
