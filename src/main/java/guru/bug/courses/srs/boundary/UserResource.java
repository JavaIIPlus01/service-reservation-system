package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.boundary.api.token.Auth;
import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.entity.UserEntity;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;

@Path("/users")
public class UserResource {
    // TODO Tests should be added
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserControl userControl;

    @Inject
    JsonWebToken token;


    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserEntity createUser(@Valid @NotNull UserEntity user) {
        LOG.info("Creating new user with name '{}'", user.getLoginName());
        //IntelliJ Idea underlines with red color 'createUser' and wants to make 'UserControl.CreateUser' static.
        return UserControl.createUser(user);
    }

    @GET
    @RolesAllowed({"admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserEntity> getUsers(UUID id, String loginName, String firstName, String lastName, String phone, String email) {
        LOG.debug("Selecting list of users");
        return userControl.findAll();
    }

    @GET
    @Path("/users/{userId}")
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // TODO Potential security breach. Any registered user can access to data of any other users. Need to restrict access for non-admins to own data only.
    public UserEntity getUserById(@Valid Auth auth, @Context SecurityContext ctx, @PathParam("id") UUID id, String loginName, String firstName, String lastName, String phone, String email) {
        var userName = ctx.getUserPrincipal().getName();
        if (ctx.isUserInRole("user")) {
// I don't know how to do this task.
        }
        LOG.debug("Selecting a single user");
        //IntelliJ Idea underlines with red color 'getUserById' and wants to make 'UserControl.getUserById' static.
        return UserControl.getUserById(id);
    }

    @PUT
    @Path("/users/{userId}")
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // TODO Same as above - any user can update data of any other user. This is allowed for admins only. Simple users can update only their own data.
    //Security breach still not solved.
    public UserEntity updateUser(@PathParam("id") UUID id, String loginName, String firstName, String lastName, String phone, String email) {
        LOG.debug("Updating the user");
        //IntelliJ Idea underlines with red color 'updateUser' and wants to make 'UserControl.UpdateUser' static.
        return UserControl.updateUser(id);
    }
}
