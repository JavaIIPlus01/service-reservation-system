package guru.bug.courses.srs.boundary.api.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import guru.bug.courses.srs.entity.RoleEntity;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

    private UUID id;

    private String name;

    public RoleDTO() {
    }

    public RoleDTO(RoleEntity savedRole) {
        this.id = savedRole.getId();
        this.name = savedRole.getName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
