package com.victorxavier.course_platform.authuser.services.impl;

import com.victorxavier.course_platform.authuser.enums.RoleType;
import com.victorxavier.course_platform.authuser.models.RoleModel;
import com.victorxavier.course_platform.authuser.repositories.RoleRepository;
import com.victorxavier.course_platform.authuser.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;


    @Override
    public Optional<RoleModel> findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType);
    }

}
