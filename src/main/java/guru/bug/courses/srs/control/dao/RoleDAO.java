package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.RoleEntity;
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
public class RoleDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RoleDAO.class);

    @PersistenceContext
    EntityManager em;

    public RoleEntity createRole(String name) {
        var id = UUID.randomUUID();
        LOG.debug("Creating role {} with name {}", id, name);
        var result = new RoleEntity();
        result.setId(id);
        result.setName(name);
        em.persist(result);
        return result;
    }

    public Optional<RoleEntity> findRoleByName(String name) {
        LOG.debug("Searching role by name {}", name);
        var result = em.createQuery("select r from RoleEntity r where r.name = :name", RoleEntity.class)
                .setParameter("name", name)
                .getResultStream()
                .findAny();
        result.ifPresentOrElse(
                r -> LOG.debug("Role {} found {}", name, r),
                () -> LOG.debug("Role {} not found", name));
        return result;
    }

    public List<RoleEntity> findAll() {
        LOG.debug("Selecting all roles");
        var result = em.createQuery("select r from RoleEntity r order by r.name", RoleEntity.class)
                .getResultList();
        LOG.debug("Selected {} roles", result.size());
        return result;
    }

}
