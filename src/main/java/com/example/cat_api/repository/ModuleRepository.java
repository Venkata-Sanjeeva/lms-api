package com.example.cat_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cat_api.model.Course;
import com.example.cat_api.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Long>{
	// Get all modules for a course, ordered for the curriculum view
    List<Module> findByCourseOrderBySequenceOrderAsc(Course course);
    
    // Alternative using course ID
    List<Module> findByCourseIdOrderBySequenceOrderAsc(Long courseId);

    Optional<Module> findByModuleUID(String moduleUID);

    @Query("SELECT COALESCE(MAX(m.sequenceOrder), 0) FROM Module m WHERE m.course.id = :courseId")
    Integer findMaxSequenceOrderByCourseId(@Param("courseId") Long courseId);
}
