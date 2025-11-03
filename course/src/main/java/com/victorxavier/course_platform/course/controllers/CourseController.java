package com.victorxavier.course_platform.course.controllers;

import com.victorxavier.course_platform.course.dtos.CourseDTO;
import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.services.CourseService;
import com.victorxavier.course_platform.course.specifications.SpecificationTemplate;
import com.victorxavier.course_platform.course.validation.CourseValidator;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseValidator courseValidator;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO, Errors errors) {
        log.debug("POST saveCourse courseDTO received {} ", courseDTO.toString());
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseService.save(courseModel);
        log.debug("POST saveCourse courseId saved {}", courseModel.getCourseId());
        log.info("Course saved successfully courseId {}", courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        log.debug("DELETE deleteCourse courseId received {}", courseId);
        CourseModel courseModel = courseService.findById(courseId);
        courseService.delete(courseModel);

        log.debug("DELETE deleteCourse courseId deleted {}", courseId);
        log.info("Course deleted successfully courseId {}", courseId);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid CourseDTO courseDTO) {
        log.debug("PUT updateCourse courseDTO received {}", courseDTO.toString());
        CourseModel courseModel = courseService.findById(courseId);
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseService.save(courseModel);
        log.debug("PUT updateCourse courseId saved {}", courseModel.getCourseId());
        log.info("Course updated successfully courseId {}", courseModel.getCourseId());
        return ResponseEntity.ok(courseModel);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page= 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
                                                           Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        if (userId != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        CourseModel courseModel = courseService.findById(courseId);
        return ResponseEntity.ok(courseModel);
    }
}