package com.victorxavier.course_platform.course.controllers;

import com.victorxavier.course_platform.course.dtos.SubscriptionDTO;
import com.victorxavier.course_platform.course.dtos.NotificationCommandDTO;
import com.victorxavier.course_platform.course.enums.UserStatus;
import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.UserModel;
import com.victorxavier.course_platform.course.publishers.NotificationCommandPublisher;
import com.victorxavier.course_platform.course.services.CourseService;
import com.victorxavier.course_platform.course.services.UserService;
import com.victorxavier.course_platform.course.specifications.SpecificationTemplate;
import com.victorxavier.course_platform.course.exception.DataConflictException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationCommandPublisher notificationCommandPublisher;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserModel>> getAllUsersByCourse(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId) {
        log.debug("GET getAllUsersByCourse courseId received {}", courseId);

        return ResponseEntity.ok(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        log.debug("POST saveSubscriptionUserInCourse subscriptionDTO received {}", subscriptionDTO.toString());
        CourseModel courseModel = courseService.findById(courseId);
        UserModel userModel = userService.findById(subscriptionDTO.userId());

        if (courseService.existsByCourseAndUser(courseId, subscriptionDTO.userId())) {
            throw new DataConflictException("Error: subscription already exists!");
        }

        if (userModel.getUserStatus().equals(UserStatus.BLOCKED.toString())) {
            throw new IllegalStateException("User is blocked.");
        }

        courseService.saveSubscriptionUserInCourse(courseModel.getCourseId(), userModel.getUserId());

        try {
            var notificationCommandDto = new NotificationCommandDTO(
                    "Bem-Vindo(a) ao Curso: " + courseModel.getName(),
                    "Olá " + userModel.getFullName() + "! A sua inscrição foi realizada com sucesso!",
                    userModel.getUserId()
            );
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);
        } catch (Exception e) {
            log.warn("Falha ao enviar notificação de inscrição: {}", e.getMessage());
        }

        log.debug("POST saveSubscriptionUserInCourse userId subscribed {}", userModel.getUserId());
        log.info("Subscription created successfully for userId {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully");
    }
}