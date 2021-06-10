package guru.bug.courses.srs.control;

import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Singleton
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

    @Inject
    UserDAO userDAO;

    @PersistenceContext
    private EntityManager em;

    public static UserEntity getUserById(UUID id) {
        LOG.debug("Searching for user by id: {}", id);
        return em.createQuery("select u from UserEntity u where u.id like :id", UserEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public static UserEntity updateUser(UUID id) {
        LOG.debug("Updating user by id: {}", id);
        UserEntity existingUser = getUserById(id);
        existingUser.setLoginName();
        existingUser.setFirstName();
        existingUser.setPhone();
        existingUser.setEmail();
        existingUser.setSalt();
        existingUser.setPasswordHash();
        em.persist(existingUser);
        return existingUser;
    }

    public UserEntity createUser(UserEntity user) {
        var userObj = userDAO.createUser(user.getLoginName());
        LOG.debug("User {} created", userObj.getLoginName());
        return userObj;

    }

    @Transactional
    public UserEntity findByLogin(String login) {
        LOG.debug("Searching for user by login: {}", login);
        return em.createQuery("select u from UserEntity u where u.loginName like :loginName", UserEntity.class)
                .setParameter("loginName", login.trim())
                .getSingleResult();
    }

    public List<UserEntity> findAll() {
        var result = userDAO.findAll();
        LOG.debug("Selected list of {} users", result.size());
        return result;
    }
}
