package com.victorxavier.course_platform.course.validation;

import com.victorxavier.course_platform.course.configs.security.AuthenticationCurrentUserService;
import com.victorxavier.course_platform.course.dtos.CourseDTO;
import com.victorxavier.course_platform.course.enums.UserType;
import com.victorxavier.course_platform.course.exception.ResourceNotFoundException;
import com.victorxavier.course_platform.course.models.UserModel;
import com.victorxavier.course_platform.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    AuthenticationCurrentUserService authenticationCurrentUserService;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

        CourseDTO courseDTO = (CourseDTO) o;
        validator.validate(courseDTO, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDTO.userInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (currentUserId.equals(userInstructor)) {

            try {
                UserModel userModel = userService.findById(userInstructor);
                if (userModel.getUserType().equals(UserType.STUDENT.toString())) {
                    errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
                }

            } catch (ResourceNotFoundException e) {
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
            }

        } else {
            errors.rejectValue("userInstructor", "UserInstructorError", "Access denied. You can only create courses for yourself.");
        }
    }
}
