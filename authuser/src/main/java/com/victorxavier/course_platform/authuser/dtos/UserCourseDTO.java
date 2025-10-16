package com.victorxavier.course_platform.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseDTO(
        UUID userId,
        @NotNull UUID courseId
) {
}
