package com.victorxavier.course_platform.authuser.services;

import com.victorxavier.course_platform.authuser.enums.RoleType;
import com.victorxavier.course_platform.authuser.models.RoleModel;

import java.util.Optional;

public interface RoleService {

    Optional<RoleModel> findByRoleName(RoleType roleType);

}
