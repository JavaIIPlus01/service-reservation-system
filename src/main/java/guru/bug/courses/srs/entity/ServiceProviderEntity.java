package guru.bug.courses.srs.entity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "SERVICE_PROVIDERS")
public class ServiceProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "master_user_id")
    private Integer master;
    @Column(name = "service_id")
    private Integer service;
    @Column(name = "notes")
    private String notes;

    @ManyToMany(mappedBy = "serviceProviderEntitySet")
    Set<ServiceEntity> serviceEntitySet;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    public Integer getService() {
        return service;
    }

    public void setService(Integer service) {
        this.service = service;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}