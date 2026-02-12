package com.example.cat_api.request;

import lombok.Data;

@Data
public class RecommendationRequest {
    private String goal;         // e.g., "Web Development", "Data Science"
    private String experience;   // e.g., "BEGINNER", "INTERMEDIATE"
    private String preferredLang; // e.g., "Java", "Python"
    private Integer timeCommitment; // e.g., hours per week
}