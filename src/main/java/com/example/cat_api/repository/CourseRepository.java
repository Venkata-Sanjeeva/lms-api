package com.example.cat_api.repository;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{
	Optional<Course> findByCourseUniqueId(String courseUniqueId);
	
	boolean existsByDifficultyAndTitleContainingIgnoreCase(Difficulty difficulty, String title);
    
    // For the course catalog
    List<Course> findByDifficulty(Difficulty difficulty);
    List<Course> findByLanguage(String language);
    
    // Search functionality
    List<Course> findByTitleContainingIgnoreCase(String title);
    
 // Finds courses that match BOTH difficulty AND title
    List<Course> findByDifficultyAndTitleContainingIgnoreCase(Difficulty difficulty, String title);

	List<Course> findByTitleContainingIgnoreCaseAndDifficulty(String goal, Difficulty difficulty);
}
