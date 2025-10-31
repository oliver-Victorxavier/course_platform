package com.victorxavier.course_platform.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record LessonDTO(
        @NotBlank String title,

        String description,

        @NotBlank String videoUrl
) {}
