package com.victorxavier.course_platform.course.services;

import com.victorxavier.course_platform.course.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel save(UserModel usermodel);

    void delete(UUID userId);

    Optional<UserModel> findById(UUID userInstructor);
}
