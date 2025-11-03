package com.victorxavier.course_platform.course.services.impl;

import com.victorxavier.course_platform.course.exception.ResourceNotFoundException;
import com.victorxavier.course_platform.course.models.LessonModel;
import com.victorxavier.course_platform.course.models.ModuleModel;
import com.victorxavier.course_platform.course.repositories.LessonRepository;
import com.victorxavier.course_platform.course.repositories.ModuleRepository;
import com.victorxavier.course_platform.course.services.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    private final LessonRepository lessonRepository;

    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }


    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        logger.info("Initiating deletion for module with ID: {}", moduleModel.getModuleId());

        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

        if (!lessonModelList.isEmpty()) {
            logger.debug("Found {} lessons related to module {}. Deleting them.", lessonModelList.size(), moduleModel.getModuleId());
            lessonRepository.deleteAll(lessonModelList);
        } else {
            logger.debug("No lessons found related to module {}. Proceeding to delete module.", moduleModel.getModuleId());
        }

        moduleRepository.delete(moduleModel);
        logger.info("Deletion completed for module with ID: {}", moduleModel.getModuleId());
    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return moduleRepository.save(moduleModel);
    }

    @Override
    public ModuleModel findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Module with ID %s not found for course %s", moduleId, courseId)
                ));
    }

    @Override
    public List<ModuleModel> findAllByCourseId(UUID courseId) {
        return moduleRepository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public ModuleModel findById(UUID moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Module with ID %s not found", moduleId)));
    }

    @Override
    public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
        return moduleRepository.findAll(spec, pageable);
    }



}