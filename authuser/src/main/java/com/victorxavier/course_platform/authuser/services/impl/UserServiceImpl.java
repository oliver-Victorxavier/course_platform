package com.victorxavier.course_platform.authuser.services.impl;

import com.victorxavier.course_platform.authuser.enums.ActionType;
import com.victorxavier.course_platform.authuser.exception.DataConflictException;
import com.victorxavier.course_platform.authuser.exception.ResourceNotFoundException;
import com.victorxavier.course_platform.authuser.models.UserModel;
import com.victorxavier.course_platform.authuser.publishers.UserEventPublisher;
import com.victorxavier.course_platform.authuser.repositories.UserRepository;
import com.victorxavier.course_platform.authuser.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEventPublisher userEventPublisher;


    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserModel findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %s not found.", userId)));
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {

        userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec,Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public UserModel saveUser(UserModel userModel) {

        if (userRepository.existsByUsername(userModel.getUsername())) {
            throw new DataConflictException(String.format("Username '%s' is already in use", userModel.getUsername()));
        }
        if (userRepository.existsByEmail(userModel.getEmail())) {
            throw new DataConflictException(String.format("Email '%s' is already in use", userModel.getEmail()));
        }

        userModel = save(userModel);
        userEventPublisher.publishUserEvent(userModel.convertToUserEventDTO(), ActionType.CREATE);
        return userModel;

    }

    @Transactional
    @Override
    public void deleteUser(UserModel userModel) {
        delete(userModel);
        userEventPublisher.publishUserEvent(userModel.convertToUserEventDTO(), ActionType.DELETE);
    }

    @Transactional
    @Override
    public UserModel updateUser(UserModel userModel) {

        userModel = save(userModel);
        userEventPublisher.publishUserEvent(userModel.convertToUserEventDTO(), ActionType.UPDATE);
        return userModel;
    }

    @Override
    public UserModel updatePassword(UserModel userModel) {
        return save(userModel);
    }

}