package com.victorxavier.course_platform.course.controllers;

import com.victorxavier.course_platform.course.dtos.ModuleDTO;
import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.ModuleModel;
import com.victorxavier.course_platform.course.services.CourseService;
import com.victorxavier.course_platform.course.services.ModuleService;
import com.victorxavier.course_platform.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/courses/{courseId}/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;


    @PostMapping
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDTO moduleDTO) {
        log.debug("POST saveModule moduleDTO received {}", moduleDTO.toString());
        CourseModel courseModel = courseService.findById(courseId);

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModel);

        moduleService.save(moduleModel);
        log.debug("POST saveModule moduleId saved {}", moduleModel.getModuleId());
        log.info("Module saved successfully moduleId {}", moduleModel.getModuleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleModel);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        log.debug("DELETE deleteModule moduleId received {}", moduleId);
        ModuleModel moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);

        moduleService.delete(moduleModel);
        log.debug("DELETE deleteModule moduleId deleted {}", moduleId);
        log.info("Module deleted successfully moduleId {}", moduleId);
        return ResponseEntity.ok("Module deleted successfully");
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDTO moduleDTO) {
        log.debug("PUT updateModule moduleDTO received {}", moduleDTO.toString());
        ModuleModel moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);

        moduleModel.setTitle(moduleDTO.title());
        moduleModel.setDescription(moduleDTO.description());
        moduleService.save(moduleModel);
        log.debug("PUT updateModule moduleId saved {}", moduleModel.getModuleId());
        log.info("Module updated successfully moduleId {}", moduleModel.getModuleId());
        return ResponseEntity.ok(moduleModel);
    }

    @GetMapping
    public ResponseEntity<Page<ModuleModel>> getAllModulesByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                                   SpecificationTemplate.ModuleSpec spec,
                                                                   @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("GET getAllModulesByCourse courseId received {}", courseId);
        return ResponseEntity.ok(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        ModuleModel moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        return ResponseEntity.ok(moduleModel);
    }
}