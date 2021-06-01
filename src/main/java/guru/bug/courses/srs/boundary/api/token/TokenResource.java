package guru.bug.courses.srs.boundary.api.token;

import guru.bug.courses.srs.control.PasswordHashEngine;
import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.control.token.TokenData;
import guru.bug.courses.srs.control.token.TokenIssuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path("/tokens")
public class TokenResource {

    private static final Logger LOG = LoggerFactory.getLogger(TokenResource.class);

    @Inject
    UserControl userControl;

    @Inject
    PasswordHashEngine passwordHashEngine;

    @Inject
    TokenIssuer tokenIssuer;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public TokenData login(@Valid @NotNull Auth auth) throws Exception {
        var user = userControl.searchByLogin(auth.getLogin())
                .orElseThrow(() -> new NotAuthorizedException(auth.getLogin()));
        LOG.debug("Trying to obtain token for user {}", user.getLoginName());
        byte[] passwordHash = passwordHashEngine.hash(auth.getPassword(), user.getSalt());
        if (!Arrays.equals(passwordHash, user.getPasswordHash())) {
            throw new NotAuthorizedException(user.getLoginName());
        }
        return tokenIssuer.provideToken(user);
    }
}
