package com.victorxavier.course_platform.course.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String createUrlGetAllUserByCourse(UUID courseId, Pageable pageable);
}
