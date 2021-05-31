package guru.bug.courses.srs.boundary.api.token;

import javax.validation.constraints.NotBlank;

public class Auth {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


}
