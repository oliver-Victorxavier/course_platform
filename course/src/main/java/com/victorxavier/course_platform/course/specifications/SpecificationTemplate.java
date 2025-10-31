package com.victorxavier.course_platform.course.specifications;

import com.victorxavier.course_platform.course.models.CourseModel;
import com.victorxavier.course_platform.course.models.UserModel;
import com.victorxavier.course_platform.course.models.LessonModel;
import com.victorxavier.course_platform.course.models.ModuleModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)})
    public interface CourseSpec extends Specification<CourseModel> {}

    @And({
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "userType", spec = Equal.class)})
    public interface UserSpec extends Specification<UserModel> {}


    @Spec(path = "title", spec = Equal.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Equal.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> courseModules = course.get("modules");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, courseModules));
        };
    }

    public static Specification<LessonModel> lessonModeleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<UserModel, CourseModel> courseJoin = root.join("courses");
            return cb.equal(courseJoin.get("courseId"), courseId);
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<CourseModel, UserModel> userJoin = root.join("users");
            return cb.equal(userJoin.get("userId"), userId);
        };
    }

}