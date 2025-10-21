package com.victorxavier.course_platform.notification.repositories;

import com.victorxavier.course_platform.notification.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
}
