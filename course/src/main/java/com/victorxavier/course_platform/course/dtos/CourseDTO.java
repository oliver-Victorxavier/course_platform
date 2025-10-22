package com.victorxavier.course_platform.course.dtos;

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
        UUID userInstructor,

        @NotNull
        CourseStatus courseStatus,
        @NotNull
        CourseLevel courseLevel

) {
}