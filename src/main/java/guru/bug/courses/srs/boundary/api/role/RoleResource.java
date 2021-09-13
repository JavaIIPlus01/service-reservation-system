package guru.bug.courses.srs.boundary.api.role;

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
import java.util.stream.Collectors;

@Path("/roles")
@RolesAllowed({"admin"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleControl roleControl;

    @POST
    public RoleDTO createRole(@Valid @NotNull RoleEntity role) {
        LOG.info("Creating new role with name '{}'", role.getName());
        RoleEntity savedRole = roleControl.createRole(role);
        return new RoleDTO(savedRole);
    }

    @GET
    public List<RoleDTO> getRoles() {
        LOG.debug("Selecting list of roles...");
        return roleControl.findAll().stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
    }

}
