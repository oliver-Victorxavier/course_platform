package com.victorxavier.course.dto;

import jakarta.validation.constraints.NotBlank;

public record LessonDTO(
        @NotBlank String tittle,

        String description,

        @NotBlank String videoUrl
) {}
