package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.control.RoleControl;
import guru.bug.courses.srs.entity.RoleEntity;
import io.smallrye.common.constraint.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/roles")
public class RoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleControl roleControl;

    @POST
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RoleEntity createRole(@Valid @NotNull RoleEntity role) {
        LOG.info("Creating new role with name '{}'", role.getName());
        return roleControl.createRole(role);
    }

    @GET
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoleEntity> getRoles() {
        LOG.debug("Selecting list of roles...");
        return roleControl.findAll();
    }

}
