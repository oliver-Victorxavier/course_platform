package com.victorxavier.course_platform.notification.dtos;

import java.util.UUID;

public record NotificationCommandDTO(

        String title,
        String message,
        UUID userId
) {
}
