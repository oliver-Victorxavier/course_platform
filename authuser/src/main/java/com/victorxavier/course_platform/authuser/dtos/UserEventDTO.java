package com.victorxavier.course_platform.authuser.dtos;

import java.util.UUID;

public record UserEventDTO(

        UUID userId,
        String username,
        String email,
        String fullName,
        String userStatus,
        String userType,
        String phoneNumber,
        String cpf,
        String imageUrl,
        String actionType
) { }
