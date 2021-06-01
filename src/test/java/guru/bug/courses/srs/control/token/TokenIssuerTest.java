package guru.bug.courses.srs.control.token;

import guru.bug.courses.srs.entity.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TokenIssuerTest {

    @Inject
    TokenIssuer issuer;

    @Test
    void whenLoginExistProvideToken() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setLoginName("Login");
        TokenData token = issuer.provideToken(user);
        assertEquals(token.getToken().length(), 643);
        assertTrue(token.getExpires().isAfter(ZonedDateTime.now()));
    }

    @Test
    void whenNoLoginThrowException() {
        UserEntity user = new UserEntity();
        assertThrows(Exception.class, () -> issuer.provideToken(user));
    }
}