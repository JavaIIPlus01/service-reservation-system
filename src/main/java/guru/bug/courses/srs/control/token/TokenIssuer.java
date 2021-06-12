package guru.bug.courses.srs.control.token;

import guru.bug.courses.srs.control.PasswordHashEngine;
import guru.bug.courses.srs.control.UserControl;
import guru.bug.courses.srs.control.exception.ServiceException;
import guru.bug.courses.srs.entity.RoleEntity;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.NotAuthorizedException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class TokenIssuer {

    private static final Logger LOG = LoggerFactory.getLogger(TokenIssuer.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserControl userControl;

    @Inject
    PasswordHashEngine passwordHashEngine;

    public TokenData provideToken(String loginName, String password) throws ServiceException {
        var user = userControl.searchByLogin(loginName)
                .orElseThrow(() -> new NotAuthorizedException(loginName));
        LOG.debug("Trying to obtain token for user {} {}", user.getFirstName(), user.getLastName());
        byte[] passwordHash = passwordHashEngine.hash(password, user.getSalt());
        if (!Arrays.equals(passwordHash, user.getPasswordHash())) {
            throw new NotAuthorizedException(user.getLoginName());
        }
        var expires = ZonedDateTime.now().plusMonths(3);
        LOG.debug("Creating jwt token for user {}", user.getLoginName());
        String token = Jwt.issuer(issuer)
                .upn(user.getLoginName())
                .groups(user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()))
                .claim("uid", user.getId().toString())
                .expiresAt(expires.toInstant())
                .sign();

        return new TokenData(token, expires);
    }
}
