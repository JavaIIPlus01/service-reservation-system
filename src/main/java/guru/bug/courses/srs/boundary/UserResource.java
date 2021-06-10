package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.entity.UserEntity;
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
import java.util.UUID;

@Path("/users")
public class UserResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserControl userControl;

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserEntity createUser(@Valid @NotNull UserEntity user) {
        LOG.info("Creating new user with name '{}'", user.getLoginName());
        return UserControl.createUser(user);
    }

    @GET
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserEntity> getUsers() {
        LOG.debug("Selecting list of users");
        return userControl.findAll();
    }

    @GET
    @Path("/users/{userId}")
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserEntity getUserById(@PathParam("id") UUID id) {
        LOG.debug("Selecting a single user");
        return UserControl.getUserById(id);
    }

    @PUT
    @Path("/users/{userId}")
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserEntity updateUser(@PathParam("id") UUID id) {
        LOG.debug("Updating the user");
        return UserControl.updateUser(id);
    }
}
