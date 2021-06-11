package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Transactional
public class UserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UserDAO.class);

    @PersistenceContext
    EntityManager em;

    public UserEntity createUser(String loginName, byte[] salt, byte[] pwdHash) {
        var id = UUID.randomUUID();
        LOG.debug("Creating user {} with login {}", id, loginName);
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setLoginName(loginName);
        user.setSalt(salt);
        user.setPasswordHash(pwdHash);
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

    List<UserEntity> findAll() {
        LOG.debug("Selecting all users");
        var result = em.createQuery("select u from UserEntity u", UserEntity.class)
                .getResultList();
        LOG.debug("Selected {} users", result.size());
        return result;
    }
}
