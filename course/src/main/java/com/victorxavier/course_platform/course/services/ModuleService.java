package com.victorxavier.course_platform.course.services;

import com.victorxavier.course_platform.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    ModuleModel findModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAllByCourseId(UUID courseId);

    ModuleModel findById(UUID moduleId);

    Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable);
}
