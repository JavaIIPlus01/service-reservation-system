package guru.bug.courses.srs.boundary.api.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import guru.bug.courses.srs.entity.RoleEntity;
import guru.bug.courses.srs.entity.UserEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private UUID id;

    private String loginName;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String password;

    private List<String> roles;

    public User() {
    }

    public User(UserEntity savedUser) {
        this.id = savedUser.getId();
        this.loginName = savedUser.getLoginName();
        this.firstName = savedUser.getFirstName();
        this.lastName = savedUser.getLastName();
        this.phone = savedUser.getPhone();
        this.email = savedUser.getEmail();
        this.roles = savedUser.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + loginName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
