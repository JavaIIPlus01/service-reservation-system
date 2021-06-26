package guru.bug.courses.srs.boundary;

import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.entity.UserEntity;
import org.eclipse.microprofile.jwt.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserControl service;

    @Inject
    @Claim("uid")
    String userIdClaim;

    @POST
    @PermitAll
    public UserEntity register(@NotNull User user) throws ServiceException {
        LOG.debug("Creating new user with login '{}'", user.getLogin());
        UserEntity savedUser = service.create(user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
        return savedUser.publicInfo();
    }

    @GET
    @RolesAllowed({"admin"})
    public List<UserEntity> getUsers() {
        return service.getAll().stream()
                .map(UserEntity::publicInfo)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{userId}")
    @PermitAll
    public Optional<UserEntity> getServiceById(@PathParam("userId") UUID id) {
        if (userIdClaim == null) {
            throw new NotAuthorizedException("Token is not valid.");
        }
        var idFromClaim = UUID.fromString(userIdClaim);
        if (!id.toString().equals(userIdClaim) && !service.isAdmin(idFromClaim)) {
            throw new NotAuthorizedException(id);
        }
        LOG.debug("Searching for user by id {}", id);
        return service.findById(id);
    }

    @PUT
    @Path("/{userId}")
    @PermitAll
    public Optional<UserEntity> updateUser(@PathParam("userId") UUID id, User user) throws ServiceException {
        if (userIdClaim == null) {
            throw new NotAuthorizedException("Token is not valid.");
        }
        var idFromClaim = UUID.fromString(userIdClaim);
        boolean isAdmin = service.isAdmin(idFromClaim);
        if (!id.toString().equals(userIdClaim) && !isAdmin) {
            throw new NotAuthorizedException(id);
        }
        var roles = user.getRoles();
        if (Objects.nonNull(roles) && (!isAdmin || id.toString().equals(userIdClaim))) {
            throw new NotAuthorizedException(roles);
        }
        var login = user.getLogin();
        var firstName = user.getFirstName();
        var lastName = user.getLastName();
        var phone = user.getLastName();
        var email = user.getPhone();
        var password = user.getPassword();
        LOG.debug("Updating user id {} -> user {}", id, user);
        return !Objects.isNull(password) ? service.updateUser(id, login, firstName, lastName, phone, email, roles, password):
                service.updateUser(id, login, firstName, lastName, phone, email, roles);
    }

}