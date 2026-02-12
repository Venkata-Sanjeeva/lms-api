package com.example.cat_api.response;

import com.example.cat_api.enums.Difficulty;

public record CourseRecommendationResponse(
        String courseUniqueId,
        String title,
        String description,
        Difficulty difficulty,
        String language
) {}