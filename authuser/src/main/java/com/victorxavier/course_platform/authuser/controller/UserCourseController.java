package com.victorxavier.course_platform.authuser.controller;

import com.victorxavier.course_platform.authuser.clients.CourseClient;
import com.victorxavier.course_platform.authuser.models.UserModel;
import com.victorxavier.course_platform.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    CourseClient courseClient;

    @Autowired
    UserService userService;


    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Object> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "userId") UUID userId,
                                                      @RequestHeader("Authorization") String token) {
        UserModel userModel = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable, token));
    }
}
