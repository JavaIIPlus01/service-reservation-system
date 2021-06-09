package guru.bug.courses.srs.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "service_providers")
public class ServiceProviderEntity implements Serializable {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "master_user_id", nullable = false)
    private UserEntity master;

    @Column(name = "notes")
    private String notes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public UserEntity getMaster() {
        return master;
    }

    public void setMaster(UserEntity master) {
        this.master = master;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProviderEntity that = (ServiceProviderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ServiceProviderEntity{" +
               "id=" + id +
               '}';
    }
}