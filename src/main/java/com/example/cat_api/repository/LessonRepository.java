package com.example.cat_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cat_api.model.Course;
import com.example.cat_api.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
	List<Lesson> findByModuleOrderBySequenceOrderAsc(Module module);
    
    // Helpful for finding the next lesson in a module
    Optional<Lesson> findByModuleAndSequenceOrder(Module module, Integer sequenceOrder);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.module.course = :course")
    long countTotalLessonsInCourse(@Param("course") Course course);
}
