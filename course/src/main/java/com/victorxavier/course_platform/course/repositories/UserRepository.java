package com.victorxavier.course_platform.course.repositories;

import com.victorxavier.course_platform.course.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

}
