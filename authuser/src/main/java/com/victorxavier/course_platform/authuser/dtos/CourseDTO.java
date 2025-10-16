package com.victorxavier.course_platform.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.victorxavier.course_platform.authuser.enums.CourseLevel;
import com.victorxavier.course_platform.authuser.enums.CourseStatus;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseDTO(

        UUID courseId,
        String name,
        String description,
        String imageUrl,
        CourseStatus courseStatus,
        CourseLevel courseLevel,
        UUID userInstructor
) {
}
