package com.victorxavier.course.dto;

import jakarta.validation.constraints.NotBlank;

public record ModuleDTO(
        @NotBlank
        String title,
        @NotBlank
        String description
) {}
