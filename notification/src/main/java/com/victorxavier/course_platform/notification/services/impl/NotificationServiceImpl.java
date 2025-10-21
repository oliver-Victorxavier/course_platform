package com.victorxavier.course_platform.notification.services.impl;

import com.victorxavier.course_platform.notification.models.NotificationModel;
import com.victorxavier.course_platform.notification.repositories.NotificationRepository;
import com.victorxavier.course_platform.notification.services.NotificationService;
import org.springframework.stereotype.Service;

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
}
