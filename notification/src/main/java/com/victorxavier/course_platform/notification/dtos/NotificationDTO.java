package com.victorxavier.course_platform.notification.dtos;

import com.victorxavier.course_platform.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public record NotificationDTO(

        @NotNull NotificationStatus notificationStatus

        ) { }
