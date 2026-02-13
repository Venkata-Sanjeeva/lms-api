package com.example.cat_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cat_api.model.LessonProgress;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // JPA will look into 'user.userUniqueId' and 'lesson.lessonUniqueId'
    Optional<LessonProgress> findByUser_UserUniqueIdAndLesson_LessonUniqueId(
        String userUid, 
        String lessonUid
    );

    // Get count using the String Course Unique ID
    @Query("SELECT COUNT(lp) FROM LessonProgress lp " +
           "JOIN lp.lesson l " +
           "JOIN l.module m " +
           "WHERE lp.user.userUniqueId = :userUid " +
           "AND m.course.courseUniqueId = :courseUid " +
           "AND lp.isCompleted = true")
    long countCompletedByUniqueIds(
        @Param("userUid") String userUid, 
        @Param("courseUid") String courseUid
    );
}
