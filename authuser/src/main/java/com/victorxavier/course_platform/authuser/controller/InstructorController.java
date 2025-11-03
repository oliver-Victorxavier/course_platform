package com.victorxavier.course_platform.authuser.controller;

import com.victorxavier.course_platform.authuser.dtos.InstructorDTO;
import com.victorxavier.course_platform.authuser.enums.UserType;
import com.victorxavier.course_platform.authuser.models.UserModel;
import com.victorxavier.course_platform.authuser.services.RoleService;
import com.victorxavier.course_platform.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDTO instructorDTO) {

        log.debug("POST saveSubscription instructorDTO received {}", instructorDTO.toString());
        UserModel userModel = userService.findById(instructorDTO.userId());

        if(userModel.getUserType().equals(UserType.INSTRUCTOR)){
            throw new IllegalArgumentException("User is already an Instructor!");
        }
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updateUser(userModel);

        log.debug("POST saveSubscription userId saved {}", userModel.getUserId());
        log.info("User updated successfully userId {}", userModel.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

}
