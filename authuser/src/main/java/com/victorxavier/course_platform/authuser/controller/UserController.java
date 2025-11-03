package com.victorxavier.course_platform.authuser.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.victorxavier.course_platform.authuser.configs.security.UserDetailsImpl;
import com.victorxavier.course_platform.authuser.dtos.UserDTO;
import com.victorxavier.course_platform.authuser.models.UserModel;
import com.victorxavier.course_platform.authuser.services.UserService;
import com.victorxavier.course_platform.authuser.specifications.SpecificationTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId",
                                                               direction = Sort.Direction.ASC) Pageable pageable,
                                                       Authentication authentication) {
        UserDetails userDetails  = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication  {}", userDetails.getUsername());
        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            for(UserModel user : userModelPage.toList()){
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        log.debug("GET getAllUsers userModelPage found {}", userModelPage);
        log.info("Users retrieved successfully totalElements {} ", userModelPage.getTotalElements());
        return ResponseEntity.ok().body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getOneUser(@PathVariable(value = "userId") UUID userId) {
        UserModel userModel = userService.findById(userId);
        return ResponseEntity.ok(userModel);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE deleteUser userId received {}", userId);
        UserModel userModel = userService.findById(userId);
        userService.deleteUser(userModel);

        log.debug("DELETE deleteUser userId deleted {}", userId);
        log.info("User deleted successfully userId {}", userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.UserPut.class)
                                             @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        log.debug("PUT updateUser userDTO received {}", userDTO.toString());
        UserModel userModel = userService.findById(userId);
        userModel.setFullName(userDTO.fullName());
        userModel.setPhoneNumber(userDTO.phoneNumber());
        userModel.setCpf(userDTO.cpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updateUser(userModel);

        log.debug("PUT updateUser userId saved {}", userModel.getUserId());
        log.info("User updated successfully userId {}", userModel.getUserId());
        return ResponseEntity.ok(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserDTO.UserView.PasswordPut.class)
                                                 @JsonView(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {
        log.debug("PUT updatePassword userDTO received {}", userDTO.toString());
        UserModel userModel = userService.findById(userId);
        if (!userModel.getPassword().equals(userDTO.oldPassword())) {
            log.warn("Mismatched old password userId {}", userId);
            throw new IllegalArgumentException("Error: Mismatched old password!");
        }
        userModel.setPassword(userDTO.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updatePassword(userModel);

        log.debug("PUT updatePassword userId saved {}", userModel.getUserId());
        log.info("Password updated successfully userId {}", userModel.getUserId());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDTO.UserView.ImagePut.class)
                                              @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        log.debug("PUT updateImage userDTO received {}", userDTO.toString());
        UserModel userModel = userService.findById(userId);
        userModel.setImageUrl(userDTO.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updateUser(userModel);

        log.debug("PUT updateImage userId saved {}", userModel.getUserId());
        log.info("Image updated successfully userId {}", userModel.getUserId());
        return ResponseEntity.ok(userModel);
    }
}