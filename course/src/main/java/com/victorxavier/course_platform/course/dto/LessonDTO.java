package com.victorxavier.course_platform.course.dto;

import jakarta.validation.constraints.NotBlank;

public record LessonDTO(
        @NotBlank String tittle,

        String description,

        @NotBlank String videoUrl
) {}
