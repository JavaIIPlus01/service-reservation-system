package guru.bug.courses.srs.control.token;


public class Auth {

    private final String username;
    private final String password;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
