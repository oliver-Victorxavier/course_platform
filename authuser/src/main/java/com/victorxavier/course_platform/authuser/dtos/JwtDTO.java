package com.victorxavier.course_platform.authuser.dtos;

import jakarta.validation.constraints.NotNull;

public record JwtDTO(

        @NotNull String token,
        String type
) {
}
