package com.victorxavier.course_platform.course.services.impl;

import com.victorxavier.course_platform.course.dtos.NotificationCommandDTO;
import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.LessonModel;
import com.victorxavier.course_platform.course.models.ModuleModel;
import com.victorxavier.course_platform.course.models.UserModel;
import com.victorxavier.course_platform.course.publishers.NotificationCommandPublisher;
import com.victorxavier.course_platform.course.repositories.CourseRepository;
import com.victorxavier.course_platform.course.repositories.LessonRepository;
import com.victorxavier.course_platform.course.repositories.ModuleRepository;
import com.victorxavier.course_platform.course.services.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    NotificationCommandPublisher notificationCommandPublisher;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {

        List<ModuleModel> moduleModelList = moduleRepository.findAllLModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()){
            for(ModuleModel module : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModel userModel) {
        courseRepository.saveCourseUser(course.getCourseId(), userModel.getUserId());

        try {
            var notificationCommandDTO = new NotificationCommandDTO(
                    "Bem-Vindo(a) ao Curso: " + course.getName(),
                    userModel.getFullName() + " a sua inscrição foi realizada com sucesso!",
                    userModel.getUserId()
            );
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDTO);
        } catch (Exception e){
            log.warn("Error sending notification command to user: " + userModel.getUserId());
        }
    }


}