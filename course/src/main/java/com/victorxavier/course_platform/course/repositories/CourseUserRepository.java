package com.victorxavier.course_platform.course.repositories;

import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

}
