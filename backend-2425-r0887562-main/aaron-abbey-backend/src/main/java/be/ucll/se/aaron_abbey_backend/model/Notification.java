package be.ucll.se.aaron_abbey_backend.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "notifications_id_seq", allocationSize = 1)
    private long id;

   private String message;

   
    private Long rentId;
    
    private Long receiverId;

    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    public enum NotificationType {
        CONFIRMATION,
        CANCELATION,
        PENDING
    }

    public Notification() {
    }

    public Notification(String message, Long rentId, Long receiverId, Date timestamp, NotificationType type) {
        this.message = message;
        this.rentId = rentId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getRentId() {
        return rentId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public NotificationType getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }


   
}
