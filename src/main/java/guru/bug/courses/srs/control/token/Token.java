package guru.bug.courses.srs.control.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    public String token;
    public LocalDateTime expires;

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }
}
