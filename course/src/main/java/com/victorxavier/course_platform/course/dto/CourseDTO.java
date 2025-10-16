package com.victorxavier.course_platform.course.dto;

import com.victorxavier.course_platform.course.enums.CourseLevel;
import com.victorxavier.course_platform.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseDTO(

        @NotBlank
        String name,

        @NotBlank
        String description,
        String imageUrl,

        @NotNull
        CourseStatus courseStatus,
        @NotNull
        CourseLevel courseLevel,
        @NotNull
        UUID userInstructor
) {
}