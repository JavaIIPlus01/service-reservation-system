package guru.bug.courses.srs.control.dao;


import guru.bug.courses.srs.entity.ServiceEntity;
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
public class ServiceDAO {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceDAO.class);

    @PersistenceContext
    EntityManager em;

    public ServiceEntity createService(String name, String description, int defaultDuration) {
        LOG.debug("Creating service {}; duration {}", name, defaultDuration);
        var result = new ServiceEntity();
        result.setId(UUID.randomUUID());
        result.setName(name);
        result.setDescription(description);
        result.setDefaultDuration(defaultDuration);
        em.persist(result);
        return result;
    }

    public Optional<ServiceEntity> findById(UUID id) {
        var service = Optional.ofNullable(em.find(ServiceEntity.class, id));
        service.ifPresentOrElse(
                s -> LOG.debug("Service {} found {}", id, s),
                () -> LOG.debug("Service {} not found", id));
        return service;
    }

    public ServiceEntity updateService(ServiceEntity service, String name, String description, int defaultDuration) {
        service.setName(name);
        service.setDescription(description);
        service.setDefaultDuration(defaultDuration);
        em.persist(service);
        LOG.debug("Updated service id {} -> name {}; description {}; duration {}",
                service.getId(), service.getName(), service. getDescription(), service.getDefaultDuration());
        return service;
    }

    public List<ServiceEntity> findAll() {
        LOG.debug("Selecting all services");
        var result = em.createQuery("select s from ServiceEntity s order by s.name", ServiceEntity.class)
                .getResultList();
        LOG.debug("Selected {} services", result.size());
        return result;
    }

}
