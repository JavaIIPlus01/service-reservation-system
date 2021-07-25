package guru.bug.courses.srs.boundary.api.service;

import guru.bug.courses.srs.boundary.api.user.User;
import guru.bug.courses.srs.control.ServiceControl;
import guru.bug.courses.srs.entity.ServiceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceResource {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    ServiceControl control;

    @POST
    @RolesAllowed({"admin"})
    public Service createService(@Valid @NotNull Service service) {
        LOG.debug("Creating new service with name '{}'", service.getName());
        ServiceEntity savedService = control.createService(service.getName(), service.getDescription(), service.getDefaultDuration());
        return new Service(savedService);
    }

    @GET
    @Path("/{serviceId}")
    @PermitAll
    public Optional<Service> getServiceById(@PathParam("serviceId") UUID id) {
        LOG.debug("Searching for service by id {}", id);
        ServiceEntity service = control.findById(id).orElse(null);
        return Objects.isNull(service) ? Optional.empty() : Optional.of(new Service(service));
    }

    @PUT
    @Path("/{serviceId}")
    @RolesAllowed({"admin"})
    public Optional<Service> updateService(@PathParam("serviceId") UUID id, Service service) {
        var name = service.getName();
        var description = service.getDescription();
        var defaultDuration = service.getDefaultDuration();
        LOG.debug("Updating service id {} -> name {}; description {}; duration {}",
                id, name, description, defaultDuration);
        ServiceEntity updatedService = control.updateService(id, name, description, defaultDuration).orElse(null);
        return Objects.isNull(updatedService) ? Optional.empty() : Optional.of(new Service(updatedService));
    }

    @GET
    @PermitAll
    public List<Service> getServices() {
        LOG.debug("Selecting list of services...");
        return control.findAll().stream()
                .map(Service::new)
                .collect(Collectors.toList());
    }

}
