package com.example.cat_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cat_api.model.Course;
import com.example.cat_api.request.RecommendationRequest;
import com.example.cat_api.service.RecommendationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/public/recommendations")
@CrossOrigin("*")
public class RecommendationController {

    private final RecommendationService recommendationService;
    
    public RecommendationController(RecommendationService recommendationService) {
    	this.recommendationService = recommendationService;
    }

    @PostMapping("/suggest")
    public ResponseEntity<List<Course>> getSuggestions(@RequestBody RecommendationRequest request) {
        return ResponseEntity.ok(recommendationService.suggestCourses(request));
    }
}