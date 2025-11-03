package com.victorxavier.course_platform.course.controllers;

import com.victorxavier.course_platform.course.dtos.LessonDTO;
import com.victorxavier.course_platform.course.models.LessonModel;
import com.victorxavier.course_platform.course.models.ModuleModel;
import com.victorxavier.course_platform.course.services.LessonService;
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
@RequestMapping("/modules/{moduleId}/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;


    @PostMapping
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDTO lessonDto) {
        log.debug("POST saveLesson lessonDto received {}", lessonDto.toString());
        ModuleModel moduleModel = moduleService.findById(moduleId);

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        lessonService.save(lessonModel);
        log.debug("POST saveLesson lessonId saved {}", lessonModel.getLessonId());
        log.info("Lesson saved successfully lessonId {}", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonModel);


    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId) {
        log.debug("DELETE deleteLesson lessonId received {}", lessonId);
        LessonModel lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);

        lessonService.delete(lessonModel);
        log.debug("DELETE deleteLesson lessonId deleted {}", lessonId);
        log.info("Lesson deleted successfully lessonId {}", lessonId);
        return ResponseEntity.ok("Lesson deleted successfully");
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDTO lessonDTO) {
        log.debug("PUT updateLesson lessonDTO received {}", lessonDTO.toString());
        LessonModel lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        lessonModel.setTitle(lessonDTO.title());
        lessonModel.setDescription(lessonDTO.description());
        lessonModel.setVideoUrl(lessonDTO.videoUrl());

        lessonService.save(lessonModel);
        log.debug("PUT updateLesson lessonId saved {}", lessonModel.getLessonId());
        log.info("Lesson updated successfully lessonId {}", lessonModel.getLessonId());
        return ResponseEntity.ok(lessonModel);
    }

    @GetMapping
    public ResponseEntity<Page<LessonModel>> getAllLessonsByModule(@PathVariable(value = "moduleId") UUID moduleId,
                                                                   SpecificationTemplate.LessonSpec spec,
                                                                   @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("GET getAllLessonsByModule moduleId received {}", moduleId);
        return ResponseEntity.ok(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId) {
        LessonModel lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        return ResponseEntity.ok(lessonModel);
    }
}