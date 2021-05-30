package guru.bug.courses.srs.control.token;

import java.time.LocalDateTime;

public class Token {

    private String token;
    private LocalDateTime expires;

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }
}
