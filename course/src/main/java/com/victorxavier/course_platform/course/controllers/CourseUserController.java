package com.victorxavier.course_platform.course.controllers;

import com.victorxavier.course_platform.course.clients.AuthUserClient;
import com.victorxavier.course_platform.course.dto.SubscriptionDTO;
import com.victorxavier.course_platform.course.dto.UserDTO;
import com.victorxavier.course_platform.course.enums.UserStatus;
import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.CourseUserModel;
import com.victorxavier.course_platform.course.services.CourseService;
import com.victorxavier.course_platform.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    AuthUserClient authUserClient;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "courseId",direction = Sort.Direction.ASC) Pageable pageable,
                                                             @PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDTO subscriptionDTO){
        ResponseEntity<UserDTO> responseUser;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.userId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDTO.userId());
            if (responseUser.getBody().userStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked!");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: User not found.");
            }
        }
        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.userId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
        }

}
