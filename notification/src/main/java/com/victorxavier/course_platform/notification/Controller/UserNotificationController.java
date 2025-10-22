package com.victorxavier.course_platform.notification.Controller;

import com.victorxavier.course_platform.notification.dtos.NotificationDTO;
import com.victorxavier.course_platform.notification.models.NotificationModel;
import com.victorxavier.course_platform.notification.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserNotificationController {

    final NotificationService notificationService;

    public UserNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable(value = "userId")UUID userId,
                                                                             @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllNotificationsByUser(userId, pageable));

    }

    @PutMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable(value = "userId") UUID userId,
                                                     @PathVariable(value = "notificationId") UUID notificationId,
                                                     @RequestBody @Valid NotificationDTO  notificationDTO) {
        Optional<NotificationModel> notificationModelOptional = notificationService.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification Not Found");
        }
        notificationModelOptional.get().setNotificationStatus(notificationDTO.notificationStatus());
        notificationService.saveNotification(notificationModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(notificationModelOptional.get());
    }
}
