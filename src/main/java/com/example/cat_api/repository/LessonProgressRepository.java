package com.example.cat_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cat_api.model.Course;
import com.example.cat_api.model.Lesson;
import com.example.cat_api.model.LessonProgress;
import com.example.cat_api.model.User;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long>{
	// Find specific progress to toggle 'isCompleted'
    Optional<LessonProgress> findByUserAndLesson(User user, Lesson lesson);
    
    // 1. Get all completed lessons for a user in a specific course
    @Query("SELECT lp FROM LessonProgress lp " +
           "JOIN lp.lesson l " +
           "JOIN l.module m " +
           "WHERE lp.user = :user " +
           "AND m.course = :course " +
           "AND lp.isCompleted = true")
    List<LessonProgress> findCompletedLessonsByUserAndCourse(
        @Param("user") User user, 
        @Param("course") Course course
    );

    // 2. Count completed lessons to calculate progress
    @Query("SELECT COUNT(lp) FROM LessonProgress lp " +
           "JOIN lp.lesson l " +
           "JOIN l.module m " +
           "WHERE lp.user = :user " +
           "AND m.course = :course " +
           "AND lp.isCompleted = true")
    long countCompletedLessonsByUserAndCourse(
        @Param("user") User user, 
        @Param("course") Course course
    );
}
