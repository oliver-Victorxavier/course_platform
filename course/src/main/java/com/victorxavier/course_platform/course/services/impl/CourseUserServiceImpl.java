package com.victorxavier.course_platform.course.services.impl;

import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.CourseUserModel;
import com.victorxavier.course_platform.course.repositories.CourseUserRepository;
import com.victorxavier.course_platform.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return courseUserRepository.save(courseUserModel);
    }

}
