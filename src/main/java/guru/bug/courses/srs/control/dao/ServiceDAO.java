package guru.bug.courses.srs.control.dao;


import guru.bug.courses.srs.entity.ServiceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Singleton
@Transactional
public class ServiceDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDAO.class);

    @PersistenceContext
    EntityManager em;

    public ServiceEntity createService(String name, int defaultDuration) {
        LOG.debug("Creating service {}; duration {}", name, defaultDuration);
        var result = new ServiceEntity();
        result.setId(UUID.randomUUID());
        result.setName(name);
        result.setDefaultDuration(defaultDuration);
        em.persist(result);
        return result;
    }

    public List<ServiceEntity> findAll() {
        LOG.debug("Selecting all services");
        var result = em.createQuery("select s from ServiceEntity s order by s.name", ServiceEntity.class)
                .getResultList();
        LOG.debug("Selected {} services", result.size());
        return result;
    }

}
