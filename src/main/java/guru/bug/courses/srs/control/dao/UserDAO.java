package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.RoleEntity;
import guru.bug.courses.srs.entity.UserEntity;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Singleton
@Transactional
public class UserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UserDAO.class);
    private static final String CLIENT = "client";

    @PersistenceContext
    EntityManager em;

    @Inject
    RoleDAO roleDAO;

    public UserEntity createUser(String loginName, byte[] salt, byte[] pwdHash) {
        UserEntity user = userFromBasicInfo(loginName, salt, pwdHash);
        em.persist(user);
        return user;
    }

    public Optional<UserEntity> findByLoginName(String name) {
        LOG.debug("Searching for user by login: {}", name);
        Optional<UserEntity> user =  em.createQuery("select u from UserEntity u where u.loginName like :loginName", UserEntity.class)
                .setParameter("loginName", name.trim())
                .getResultStream()
                .findAny();
        user.ifPresentOrElse(
                u -> LOG.debug("user {} found {}", name, u),
                () -> LOG.debug("user {} not found", name));
        return user;
    }

    public Optional<UserEntity> findById(UUID id) {
        Optional<UserEntity> user = Optional.ofNullable(em.find(UserEntity.class, id));
        user.ifPresentOrElse(
                u -> LOG.debug("user {} found {}", id, u),
                () -> LOG.debug("user {} not found", id));
        return user;
    }

    public List<UserEntity> findAll() {
        LOG.debug("Selecting all users");
        var result = em.createQuery("select u from UserEntity u", UserEntity.class)
                .getResultList();
        LOG.debug("Selected {} users", result.size());
        return result;
    }

    public UserEntity createUser(String login, byte[] passwordHash, byte[] salt, String firstName, String lastName, String email, String phone) {
        RoleEntity clientRole = roleDAO.findRoleByName(CLIENT).orElseThrow(() -> new ServiceException("Couldn't find role for client"));
        UserEntity user = userFromBasicInfo(login, salt, passwordHash);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoles(Set.of(clientRole));
        em.persist(user);
        return user;
    }

    public void updateUser(UserEntity user, String login, String firstName, String lastName, String email,
                                 String phone, Set<RoleEntity> roles, byte[] pwdHash) {
        user.setLoginName(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoles(roles);
        user.setPasswordHash(pwdHash);
        em.merge(user);
        LOG.debug("Updated user id {} -> login {}; firstName {}; lastName {}; email {}; phone {}; roles {}",
                user.getId(), user.getLoginName(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhone(), user.getRoles());
    }

    private UserEntity userFromBasicInfo(String loginName, byte[] salt, byte[] pwdHash) {
        var id = UUID.randomUUID();
        LOG.debug("Creating user {} with login {}", id, loginName);
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setLoginName(loginName);
        user.setSalt(salt);
        user.setPasswordHash(pwdHash);
        return user;
    }
}
