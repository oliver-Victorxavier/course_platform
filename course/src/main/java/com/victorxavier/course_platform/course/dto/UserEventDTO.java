package com.victorxavier.course_platform.course.dto;

import com.victorxavier.course_platform.course.models.UserModel;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UserEventDTO(

        UUID userId,
        String username,
        String email,
        String fullName,
        String userStatus,
        String userType,
        String phoneNumber,
        String cpf,
        String imageUrl,
        String actionType
) {
    public UserModel convertToUserModel() {
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }
}
