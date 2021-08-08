package guru.bug.courses.srs.boundary.api.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import guru.bug.courses.srs.entity.ServiceEntity;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDTO {

    private UUID id;

    private String name;

    private String description;

    private int defaultDuration;

    public ServiceDTO() {

    }

    public ServiceDTO(ServiceEntity savedService) {
        this.id = savedService.getId();
        this.name = savedService.getName();
        this.description = savedService.getDescription();
        this.defaultDuration = savedService.getDefaultDuration();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultDuration=" + defaultDuration +
                '}';
    }
}
