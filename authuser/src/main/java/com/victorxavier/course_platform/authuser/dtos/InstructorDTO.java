package com.victorxavier.course_platform.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InstructorDTO(

        @NotNull UUID userId
        ) {
}
