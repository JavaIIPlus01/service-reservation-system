package guru.bug.courses.srs.boundary.api.user;

import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.entity.UserEntity;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
    JsonWebToken jwt;

    @POST
    @PermitAll
    public User register(@NotNull User user) throws ServiceException {
        LOG.debug("Creating new user with login '{}'", user.getLoginName());
        UserEntity savedUser = service.create(user.getLoginName(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
        return new User(savedUser);
    }

    @GET
    @RolesAllowed({"admin"})
    public List<User> getUsers() {
        return service.getAll().stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{userId}")
    @PermitAll
    public Optional<User> getUserById(@PathParam("userId") UUID id) {
        String idFromClaim = jwt.getClaim("uid");
        if (idFromClaim == null) {
            throw new NotAuthorizedException("Token is not valid.");
        }
        var claimId = UUID.fromString(idFromClaim);
        if (!id.toString().equals(idFromClaim) && !service.isAdmin(claimId)) {
            throw new NotAuthorizedException(id);
        }
        LOG.debug("Searching for user by id {}", id);
        UserEntity user = service.findById(id).orElse(null);
        return Objects.isNull(user) ? Optional.empty() : Optional.of(new User(user));
    }

    @PUT
    @Path("/{userId}")
    @PermitAll
    public Optional<User> updateUser(@PathParam("userId") UUID id, User user) throws ServiceException {
        String idFromClaim = jwt.getClaim("uid");

        if (idFromClaim == null) {
            throw new NotAuthorizedException("Token is not valid.");
        }
        var claimId = UUID.fromString(idFromClaim);
        boolean isAdmin = service.isAdmin(claimId);
        if (!id.toString().equals(idFromClaim) && !isAdmin) {
            throw new NotAuthorizedException(id);
        }
        var roles = user.getRoles();
        if (Objects.nonNull(roles) && (!isAdmin || id.toString().equals(idFromClaim))) {
            throw new NotAuthorizedException(roles);
        }
        var login = user.getLoginName();
        var firstName = user.getFirstName();
        var lastName = user.getLastName();
        var phone = user.getLastName();
        var email = user.getPhone();
        var password = user.getPassword();
        LOG.debug("Updating user id {} -> user {}", id, user);
        UserEntity updatedUser =  !Objects.isNull(password) ? service.updateUser(id, login, firstName, lastName, phone, email, roles, password).orElse(null):
                service.updateUser(id, login, firstName, lastName, phone, email, roles).orElse(null);
        return Objects.isNull(updatedUser) ? Optional.empty() : Optional.of(new User(updatedUser));
    }

}