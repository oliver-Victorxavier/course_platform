package com.victorxavier.course_platform.notification.services.impl;

import com.victorxavier.course_platform.notification.enums.NotificationStatus;
import com.victorxavier.course_platform.notification.models.NotificationModel;
import com.victorxavier.course_platform.notification.repositories.NotificationRepository;
import com.victorxavier.course_platform.notification.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public NotificationModel saveNotification(NotificationModel notificationModel) {
        return notificationRepository.save(notificationModel);
    }

    @Override
    public Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
    }

    @Override
    public Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
    }
}
