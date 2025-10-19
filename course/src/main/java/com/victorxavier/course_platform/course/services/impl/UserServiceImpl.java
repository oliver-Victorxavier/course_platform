package com.victorxavier.course_platform.course.services.impl;

import com.victorxavier.course_platform.course.repositories.UserRepository;
import com.victorxavier.course_platform.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


}
