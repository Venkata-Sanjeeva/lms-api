package com.example.cat_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cat_api.model.LessonProgress;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // JPA will look into 'user.userUID' and 'lesson.lessonUID'
    Optional<LessonProgress> findByUser_UserUIDAndLesson_LessonUID(
        String userUID, 
        String lessonUID
    );

    // Get count using the String Course Unique ID
    @Query("SELECT COUNT(lp) FROM LessonProgress lp " +
           "JOIN lp.lesson l " +
           "JOIN l.module m " +
           "WHERE lp.user.userUID = :userUID " +
           "AND m.course.courseUID = :courseUID " +
           "AND lp.isCompleted = true")
    long countCompletedByUIDs(
        @Param("userUID") String userUID, 
        @Param("courseUID") String courseUID
    );
}
