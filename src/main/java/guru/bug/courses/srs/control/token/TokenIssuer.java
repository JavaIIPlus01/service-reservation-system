package guru.bug.courses.srs.control.token;

import guru.bug.courses.srs.entity.UserEntity;
import io.smallrye.jwt.build.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.ZonedDateTime;
import java.util.Set;

@Singleton
public class TokenIssuer {

    private static final Logger LOG = LoggerFactory.getLogger(TokenIssuer.class);

    public TokenData provideToken(UserEntity user) {
        var expires = ZonedDateTime.now().plusMonths(3);
        LOG.debug("Creating jwt token for user {}", user.getLoginName());
        String token = Jwt.issuer("https://api.srs.guru.bug/issuer")
                .upn(user.getLoginName())
                .groups(Set.of("user"))
                .claim("uid", user.getId().toString())
                .expiresAt(expires.toInstant())
                .sign();

        return new TokenData(token, expires);
    }
}
