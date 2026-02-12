package com.example.cat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.model.Course;
import com.example.cat_api.repository.CourseRepository;
import com.example.cat_api.request.RecommendationRequest;

@Service
public class RecommendationService {

    private final CourseRepository courseRepository;
    
    public RecommendationService(CourseRepository courseRepository) {
    	this.courseRepository = courseRepository;
    }

    public List<Course> suggestCourses(RecommendationRequest request) {
        // Simple Logic: Find courses matching the goal in the title/description 
        // AND matching the difficulty level.
        
        Difficulty difficulty = mapExperienceToDifficulty(request.getExperience());
        
        return courseRepository.findByTitleContainingIgnoreCaseAndDifficulty(
            request.getGoal(), 
            difficulty
        );
    }

    private Difficulty mapExperienceToDifficulty(String experience) {
        return switch (experience.toUpperCase()) {
            case "ADVANCED" -> Difficulty.ADVANCED;
            case "INTERMEDIATE" -> Difficulty.INTERMEDIATE;
            default -> Difficulty.BEGINNER;
        };
    }
}