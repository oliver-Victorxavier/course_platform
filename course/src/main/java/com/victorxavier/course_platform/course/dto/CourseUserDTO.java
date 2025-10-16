package com.victorxavier.course_platform.course.dto;

import java.util.UUID;

public record CourseUserDTO (
        UUID courseId,
        UUID userId
){ }
