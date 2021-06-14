package guru.bug.courses.srs.control.token;

import java.time.ZonedDateTime;

public class TokenData {

    private String token;
    private ZonedDateTime expires;

    public TokenData(String token, ZonedDateTime expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

}
