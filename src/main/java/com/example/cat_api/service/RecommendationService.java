package com.example.cat_api.service;

import java.util.Collections;
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
        if (request == null) {
            return Collections.emptyList();
        }

        // Default values to prevent NPEs
        String goal = request.getGoal() != null ? request.getGoal() : "";
        String exp = request.getExperience() != null ? request.getExperience() : "BEGINNER";
        
        Difficulty difficulty = mapExperienceToDifficulty(exp);
        
        return courseRepository.findByTitleContainingIgnoreCaseAndDifficulty(goal, difficulty);
    }

    private Difficulty mapExperienceToDifficulty(String experience) {
        return switch (experience.toUpperCase()) {
            case "ADVANCED" -> Difficulty.ADVANCED;
            case "INTERMEDIATE" -> Difficulty.INTERMEDIATE;
            default -> Difficulty.BEGINNER;
        };
    }
}