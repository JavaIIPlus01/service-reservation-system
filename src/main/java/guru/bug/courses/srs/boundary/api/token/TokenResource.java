package guru.bug.courses.srs.boundary.api.token;

import guru.bug.courses.srs.control.PasswordHashEngine;
import guru.bug.courses.srs.control.UserControl;
import io.smallrye.jwt.build.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;

@Path("/tokens")
public class TokenResource {

    private static final Logger LOG = LoggerFactory.getLogger(TokenResource.class);

    @Inject
    UserControl userControl;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public TokenData login(@Valid Auth auth) throws Exception {
        var user = userControl.findByLogin(auth.getLogin());
        LOG.debug("Trying to obtain token for user {}", user.getLoginName());
        byte[] passwordHash = PasswordHashEngine.hash(auth.getPassword(), user.getSalt());
        if (!Arrays.equals(passwordHash, user.getPasswordHash())) {
            throw new NotAuthorizedException(user.getLoginName());
        }

        var expires = ZonedDateTime.now().plusMonths(3);
        String token = Jwt.issuer("https://api.srs.guru.bug/issuer")
                .upn(user.getLoginName())
                .groups(Set.of("user"))
                .claim("uid", user.getId().toString())
                .expiresAt(expires.toInstant())
                .sign();

        var result = new TokenData();
        result.setToken(token);
        result.setExpires(expires);
        return result;
    }

}
