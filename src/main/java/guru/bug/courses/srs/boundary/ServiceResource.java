package guru.bug.courses.srs.boundary;

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
import java.util.Optional;
import java.util.UUID;

@Path("/services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceResource {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    ServiceControl serviceControl;

    @POST
    @RolesAllowed({"admin"})
    public ServiceEntity createService(@Valid @NotNull ServiceEntity service) {
        LOG.info("Creating new service with name '{}'", service.getName());
        return serviceControl.createService(service);
    }

    @GET
    @Path("/{serviceId}")
    @PermitAll
    public Optional<ServiceEntity> getServiceById(@PathParam("serviceId") UUID id) {
        LOG.info("Searching for service by id {}", id);
        return serviceControl.findById(id);
    }

    @PUT
    @Path("/{serviceId}")
    @RolesAllowed({"admin"})
    public Optional<ServiceEntity> updateService(@PathParam("serviceId") UUID id, ServiceEntity service) {
        var name = service.getName();
        var description = service.getDescription();
        var defaultDuration = service.getDefaultDuration();
        LOG.info("Updating service id {} -> name {}; description {}; duration {}",
                id, name, description, defaultDuration);
        return serviceControl.updateService(id, name, description, defaultDuration);
    }

    @GET
    @PermitAll
    public List<ServiceEntity> getServices() {
        LOG.info("Selecting list of services...");
        return serviceControl.findAll();
    }

}
