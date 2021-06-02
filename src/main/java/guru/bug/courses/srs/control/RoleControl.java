package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.RoleDAO;
import guru.bug.courses.srs.entity.RoleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.*;

@Singleton
@Transactional
public class RoleControl {
    private static final Logger LOG = LoggerFactory.getLogger(RoleControl.class);

    @Inject
    RoleDAO roleDAO;

    public RoleEntity createRole(RoleEntity role) {
        var roleObj = roleDAO.createRole(role.getName());
        LOG.debug("Created role {}", roleObj.getName());
        return roleObj;
    }

    public List<RoleEntity> findAll() {
        var result = roleDAO.findAll();
        LOG.debug("Selected list of {} roles", result.size());
        return result;
    }
}