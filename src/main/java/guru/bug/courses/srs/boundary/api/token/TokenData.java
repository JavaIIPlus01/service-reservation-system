package guru.bug.courses.srs.boundary.api.token;

import java.time.ZonedDateTime;

public class TokenData {

    private String token;
    private ZonedDateTime expires;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }
}