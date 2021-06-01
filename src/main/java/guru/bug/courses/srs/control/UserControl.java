package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.UserDAO;
import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

   @Inject
    UserDAO userDAO;

    public Optional<UserEntity> searchByLogin(String login) {
        LOG.debug("Searching for user by login {}", login);
        return userDAO.findByLoginName(login);

    }

}
