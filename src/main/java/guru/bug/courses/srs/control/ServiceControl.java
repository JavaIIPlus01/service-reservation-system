package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.dao.ServiceDAO;
import guru.bug.courses.srs.entity.ServiceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Transactional
public class ServiceControl {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceControl.class);

    @Inject
    ServiceDAO serviceDAO;

    public ServiceEntity createService(ServiceEntity service) {
        var serviceObj = serviceDAO.createService(service.getName(), service.getDescription(), service.getDefaultDuration());
        LOG.debug("Created service {}", serviceObj.getId());
        return serviceObj;
    }

    public Optional<ServiceEntity> findById(UUID id) {
        LOG.debug("Searching for service by id {}", id);
        return serviceDAO.findById(id);
    }

    public Optional<ServiceEntity> updateService(UUID id, String name, String description, int defaultDuration) {
        var result = findById(id);
        result.ifPresentOrElse(
                s -> s = serviceDAO.updateService(s, name, description, defaultDuration),
                () -> LOG.debug("Service {} not exist", id));
        return result;
    }

    public List<ServiceEntity> findAll() {
        var result = serviceDAO.findAll();
        LOG.debug("Selected list of {} services", result.size());
        return result;
    }
}
