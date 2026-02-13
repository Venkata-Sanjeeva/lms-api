package com.example.cat_api.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonProgressResponse {
    private Long id;
    private String lessonUID;
    private String lessonTitle; // Helpful for summary pages
    private boolean isCompleted;
    private LocalDateTime completedAt;
}