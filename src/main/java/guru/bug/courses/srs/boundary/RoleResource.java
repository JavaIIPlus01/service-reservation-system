package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.control.RoleControl;
import guru.bug.courses.srs.entity.RoleEntity;
import io.smallrye.common.constraint.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("/roles")
public class RoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleControl roleControl;

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RoleEntity setRole(@Valid @NotNull RoleEntity role,
                              @Context SecurityContext sec) {
        var roleObj = new RoleEntity();
        if (sec.isUserInRole("admin")) {
            roleObj = roleControl.createRole(role);
            LOG.info("Creating new role...");
        }
        return roleObj;
    }

    @GET
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public List<RoleEntity> getRoles(@Context SecurityContext sec) {
        List<RoleEntity> roleList = new ArrayList<>();
        if (sec.isUserInRole("admin")) {
            roleList = roleControl.findAll();
            LOG.info("Selecting list of roles...");
        }
        return roleList;
    }

}
