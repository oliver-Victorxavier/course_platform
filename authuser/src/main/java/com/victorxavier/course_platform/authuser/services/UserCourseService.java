package com.victorxavier.course_platform.authuser.services;

import com.victorxavier.course_platform.authuser.models.UserCourseModel;
import com.victorxavier.course_platform.authuser.models.UserModel;

import java.util.UUID;

public interface UserCourseService {

    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    UserCourseModel save(UserCourseModel userCourseModel);

    boolean existsByCourseId(UUID courseId);

    void deleteUserCourseByCourse(UUID courseId);
}
