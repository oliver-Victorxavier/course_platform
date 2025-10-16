package com.victorxavier.course_platform.course.services;

import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);


}
