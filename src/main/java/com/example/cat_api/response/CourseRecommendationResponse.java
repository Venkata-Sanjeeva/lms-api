package com.example.cat_api.response;

import com.example.cat_api.enums.Difficulty;

public record CourseRecommendationResponse(
        String courseUID,
        String title,
        String description,
        Difficulty difficulty,
        String language
) {}