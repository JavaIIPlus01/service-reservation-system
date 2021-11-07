package guru.bug.courses.srs.control.token;

import java.time.ZonedDateTime;
import java.util.List;

public class TokenData {

    private String token;
    private ZonedDateTime expires;
    private String loginName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private List<String> roles;

    public TokenData(String token, ZonedDateTime expires, String loginName, String firstName, String lastName,
                     String phone, String email, List<String> roles) {
        this.token = token;
        this.expires = expires;
        this.loginName = loginName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}