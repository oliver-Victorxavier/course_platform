package com.victorxavier.course_platform.notification.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.victorxavier.course_platform.notification.enums.NotificationStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_NOTIFICATIONS")
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false,length = 150)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    public NotificationModel() {}

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotificationModel that = (NotificationModel) o;
        return Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(notificationId);
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", creationDate=" + creationDate +
                ", notificationStatus=" + notificationStatus +
                '}';
    }
}
