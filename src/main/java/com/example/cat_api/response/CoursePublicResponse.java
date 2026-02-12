package com.example.cat_api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursePublicResponse {
    private String courseUniqueId;
    private String title;
    private String description;
    private String difficulty;
    private String language;
    private Long enrollStudentsCount;
    private List<ModuleResponse> modules;
}