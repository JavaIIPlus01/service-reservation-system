package guru.bug.courses.srs.boundary.api.token;

import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.control.token.TokenData;
import guru.bug.courses.srs.control.token.TokenIssuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/tokens")
public class TokenResource {

    private static final Logger LOG = LoggerFactory.getLogger(TokenResource.class);

    @Inject
    TokenIssuer tokenIssuer;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public TokenData login(@Valid @NotNull Auth auth) throws ServiceException {
        LOG.info("Obtaining token for login name {}", auth.getLogin());
        return tokenIssuer.provideToken(auth.getLogin(), auth.getPassword());
    }
}
