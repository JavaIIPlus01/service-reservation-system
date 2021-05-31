package guru.bug.courses.srs.control.dao;

import guru.bug.courses.srs.entity.RoleEntity;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.UUID;

@Singleton
@Transactional
public class RoleDAO {
    @PersistenceContext
    EntityManager em;

    public RoleEntity createRole(String name) {
        var result = new RoleEntity();
        result.setId(UUID.randomUUID());
        result.setName(name);
        em.persist(result);
        return result;
    }

}
