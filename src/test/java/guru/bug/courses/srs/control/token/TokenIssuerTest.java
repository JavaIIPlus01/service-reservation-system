package guru.bug.courses.srs.control.token;

import guru.bug.courses.srs.control.UserControl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class TokenIssuerTest {

    @Inject
    TokenIssuer issuer;

    @Inject
    UserControl userControl;

    @Test
    void whenLoginExistProvideToken() throws Exception {
        userControl.create("LoginToken", "saltToken", "passwordToken");
        TokenData token = issuer.provideToken("LoginToken", "passwordToken");
        assertThat(token.getToken().length(), is(greaterThan(640)));
        assertTrue(token.getExpires().isAfter(ZonedDateTime.now()));
    }

    @Test
    void whenNoUserThrowException() {
        assertThrows(Exception.class, () -> issuer.provideToken("LoginNoExistent", "Password"));
    }
}