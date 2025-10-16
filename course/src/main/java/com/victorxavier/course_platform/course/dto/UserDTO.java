package com.victorxavier.course_platform.course.dto;

import com.victorxavier.course_platform.course.enums.UserStatus;
import com.victorxavier.course_platform.course.enums.UserType;

import java.util.UUID;

public record UserDTO(

        UUID userId,
        String username,
        String email,
        String fullName,
        UserStatus userStatus,
        UserType userType,
        String phoneNumber,
        String cpf,
        String imageUrl
) { }
