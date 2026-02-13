package com.example.cat_api.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.cat_api.response.CourseRecommendationResponse;
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

    public List<CourseRecommendationResponse> suggestCourses(RecommendationRequest request) {
        if (request == null || request.getGoal() == null) return Collections.emptyList();

        List<Course> allCourses = courseRepository.findAll();
        String goal = request.getGoal().toLowerCase();
        Difficulty userDiff = mapExperienceToDifficulty(request.getExperience());

        return allCourses.stream()
                .map(course -> {
                    double score = 0.0;
                    String title = course.getTitle().toLowerCase();
                    String desc = course.getDescription().toLowerCase();

                    // 1. Keyword Scoring (Title is weighted 2x over Description)
                    if (title.contains(goal)) score += 10;
                    if (desc.contains(goal)) score += 5;

                    // 2. Synonym Logic (Matches "Java" if user types "Backend")
                    if (matchesSynonyms(goal, title, desc)) score += 7;

                    // 3. Language Match
                    if (request.getPreferredLang() != null &&
                            course.getLanguage().equalsIgnoreCase(request.getPreferredLang())) {
                        score += 8;
                    }

                    // 4. Difficulty Logic (Using the missing helper method)
                    if (course.getDifficulty() == userDiff) {
                        score += 5;
                    } else if (isNearbyDifficulty(userDiff, course.getDifficulty())) {
                        score += 2;
                    } else {
                        score -= 10; // Stronger penalty for huge skill gaps
                    }

                    return new ScoredCourse(course, score);
                })
                .filter(sc -> sc.score() > 10)
                .sorted(Comparator.comparingDouble(ScoredCourse::score).reversed())
                .map(sc -> new CourseRecommendationResponse(
                        sc.course().getCourseUID(),
                        sc.course().getTitle(),
                        sc.course().getDescription(),
                        sc.course().getDifficulty(),
                        sc.course().getLanguage()
                ))
                .limit(5)
                .toList();
    }

    // --- HELPER METHODS ---

    private boolean isNearbyDifficulty(Difficulty user, Difficulty course) {
        // Allows Beginner to see Intermediate, and Advanced to see Intermediate
        int diff = Math.abs(user.ordinal() - course.ordinal());
        return diff == 1;
    }

    private boolean matchesSynonyms(String goal, String title, String desc) {
        // Simple example: mapping user goals to technical keywords
        if (goal.contains("backend") && (title.contains("java") || title.contains("python") || title.contains("security"))) return true;
        if (goal.contains("data") && (title.contains("python") || title.contains("math") || title.contains("science"))) return true;
        return false;
    }

    private Difficulty mapExperienceToDifficulty(String experience) {
        if (experience == null) return Difficulty.BEGINNER;
        return switch (experience.toUpperCase()) {
            case "ADVANCED" -> Difficulty.ADVANCED;
            case "INTERMEDIATE" -> Difficulty.INTERMEDIATE;
            default -> Difficulty.BEGINNER;
        };
    }

    private record ScoredCourse(Course course, double score) {}
}