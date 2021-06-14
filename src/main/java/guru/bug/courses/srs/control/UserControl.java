package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.UserDAO;
import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
@Transactional
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

    @Inject
    UserDAO userDAO;

    @Inject
    PasswordHashEngine passwordHashEngine;

    public Optional<UserEntity> searchByLogin(String login) {
        LOG.debug("Searching for user by login {}", login);
        return userDAO.findByLoginName(login);
    }

    public UserEntity create(String login, String salt, String password) throws Exception {
        return userDAO.createUser(login, salt.getBytes(), passwordHashEngine.hash(password, salt.getBytes()));
    }


}
