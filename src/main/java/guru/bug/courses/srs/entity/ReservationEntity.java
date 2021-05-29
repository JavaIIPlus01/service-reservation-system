package guru.bug.courses.srs.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "RESERVATIONS")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "service_provider_id")
    private Integer serviceProvider;
    @Column(name = "client_user_id")
    private Integer client;
    @Column(name = "scheduled_start_time")
    private LocalDateTime scheduledStartTime;
    @Column(name = "scheduled_finish_time")
    private LocalDateTime scheduledEndTime;
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;
    @Column(name = "actual_finish_time")
    private LocalDateTime actualFinishTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(Integer serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public LocalDateTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(LocalDateTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public LocalDateTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalDateTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualFinishTime() {
        return actualFinishTime;
    }

    public void setActualFinishTime(LocalDateTime actualFinishTime) {
        this.actualFinishTime = actualFinishTime;
    }
}
