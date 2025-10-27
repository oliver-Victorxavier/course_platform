package com.victorxavier.course_platform.authuser.repositories;

import com.victorxavier.course_platform.authuser.enums.RoleType;
import com.victorxavier.course_platform.authuser.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByRoleName(RoleType name);

}
