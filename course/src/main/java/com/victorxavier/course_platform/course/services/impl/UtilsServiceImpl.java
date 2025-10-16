package com.victorxavier.course_platform.course.services.impl;

import com.victorxavier.course_platform.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {


    public String createUrlGetAllUserByCourse(UUID courseId, Pageable pageable) {
        return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort" + pageable.getSort().toString().replaceAll(": ", ",");


    }
}
