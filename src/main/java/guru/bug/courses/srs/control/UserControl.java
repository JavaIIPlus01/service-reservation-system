package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.UserDAO;
import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Transactional
public class UserControl {
    // TODO Tests should be added
    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

    @Inject
    UserDAO userDAO;

    @Inject
    PasswordHashEngine passwordHashEngine;

    @PersistenceContext
    private EntityManager em;

    public Optional<UserEntity> searchByLogin(String login) {
        LOG.debug("Searching for user by login {}", login);
        //IntelliJ Idea underlines with red color 'findByLoginName' and wants to make 'UserDAO.findByLoginName' static.
        return UserDAO.findByLoginName(login);
    }

    public UserEntity create(String login, String salt, String password) throws Exception {
        return userDAO.createUser(login, salt.getBytes(), passwordHashEngine.hash(password, salt.getBytes()));
    }


    //  my code
    public UserEntity getUserById(UUID id) {
        LOG.debug("Searching for user by id: {}", id);
        return em.createQuery("select u from UserEntity u where u.id like :id", UserEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

//I have tried to complete the code. I don't know if this code is working.
    //IntelliJ Idea can't compile program.
    public UserEntity updateUser(UUID id) {
        LOG.debug("Updating user by id: {}", id);
        UserEntity existingUser = getUserById(id);
        existingUser.setLoginName(existingUser.getLoginName());
        existingUser.setFirstName(existingUser.getFirstName());
        existingUser.setPhone(existingUser.getPhone());
        existingUser.setEmail(existingUser.getEmail());
        existingUser.setSalt(existingUser.getSalt());
        existingUser.setPasswordHash(existingUser.getPasswordHash());
        em.persist(existingUser);
        return existingUser;
    }

    public UserEntity createUser(UserEntity user) {
        //IntelliJ Idea underlines with red color '(user.getLoginName())' and wants to change signature of
        // 'createUser(String, byte[], byte[])' or create method 'createUser' in 'userDAO' (but this method exists).
        var userObj = userDAO.createUser(user.getLoginName());
        LOG.debug("User {} created", userObj.getLoginName());
        return userObj;
    }


    public UserEntity findByLogin(String login) {
        LOG.debug("Searching for user by login: {}", login);
        return em.createQuery("select u from UserEntity u where u.loginName like :loginName", UserEntity.class)
                .setParameter("loginName", login.trim())
                .getSingleResult();
    }

    public List<UserEntity> findAll() {
        //IntelliJ Idea underlines with red color 'findAll' and wants to make 'UserDAO.findAll' public.
        var result = userDAO.findAll();
        LOG.debug("Selected list of {} users", result.size());
        return result;
    }
}
