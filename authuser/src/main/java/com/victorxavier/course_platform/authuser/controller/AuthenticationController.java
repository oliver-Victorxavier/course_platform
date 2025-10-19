package com.victorxavier.course_platform.authuser.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorxavier.course_platform.authuser.dtos.UserDTO;
import com.victorxavier.course_platform.authuser.enums.UserStatus;
import com.victorxavier.course_platform.authuser.enums.UserType;
import com.victorxavier.course_platform.authuser.models.UserModel;
import com.victorxavier.course_platform.authuser.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDTO.UserView.RegistrationPost.class)
                                                   @JsonView(UserDTO.UserView.RegistrationPost.class)
                                               UserDTO userDTO) {
        log.debug("POST registerUser userDTO received {}", userDTO.toString());
        if (userService.existsByUsername(userDTO.username())){
            log.warn("Username {} is already taken", userDTO.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already in use");
        }
        if (userService.existsByEmail(userDTO.email())){
            log.warn("Email {} is already taken", userDTO.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registerUser userId saved {}", userModel.getUserId());
        log.info("User saved sucessfully userId {} ", userModel.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @GetMapping("/")
    public String index(){
        log.trace("This is a trace");
        log.debug("This is a debug message");
        log.info("This is a info message");
        log.warn("This is a warn message");
        log.error("This is a error message");
        return "Logging spring boot...";

    }

}