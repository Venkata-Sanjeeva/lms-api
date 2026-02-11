package com.example.cat_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cat_api.model.Course;
import com.example.cat_api.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Long>{
	// Get all modules for a course, ordered for the curriculum view
    List<Module> findByCourseOrderBySequenceOrderAsc(Course course);
    
    // Alternative using course ID
    List<Module> findByCourseIdOrderBySequenceOrderAsc(Long courseId);
}
