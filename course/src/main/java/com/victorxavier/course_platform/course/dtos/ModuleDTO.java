package com.victorxavier.course_platform.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModuleDTO(
        @NotBlank
        String title,
        @NotBlank
        String description
) {}
