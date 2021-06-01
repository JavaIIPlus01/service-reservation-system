package guru.bug.courses.srs.control;

import guru.bug.courses.srs.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Singleton
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public UserEntity findByLogin(String login) {
        LOG.debug("Searching for user by login: {}", login);
        return em.createQuery("select u from UserEntity u where u.loginName like :loginName", UserEntity.class)
                .setParameter("loginName", login.trim())
                .getSingleResult();
    }

}
