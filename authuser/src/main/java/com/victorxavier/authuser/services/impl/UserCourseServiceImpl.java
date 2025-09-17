package com.victorxavier.authuser.services.impl;

import com.victorxavier.authuser.repositories.UserCourseRepository;
import com.victorxavier.authuser.services.UserCourseService;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    final
    UserCourseRepository userCourseRepository;

    public UserCourseServiceImpl(UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

}
