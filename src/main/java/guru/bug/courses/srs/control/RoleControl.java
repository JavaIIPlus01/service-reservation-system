package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.RoleDAO;
import guru.bug.courses.srs.entity.RoleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Singleton
@Transactional
public class RoleControl {
    private static final Logger LOG = LoggerFactory.getLogger(RoleControl.class);

    @PersistenceContext
    EntityManager em;

    @Inject
    RoleDAO roleDAO;

    public RoleEntity createRole(RoleEntity role) {
        var id = UUID.randomUUID();
        role.setId(id);
        em.persist(role);
        LOG.debug("Created role {}", role);
        return role;
    }

    public List<RoleEntity> findAll() {
        var result = roleDAO.findAll();
        LOG.debug("Selected {} roles", result.size());
        return result;
    }
}
