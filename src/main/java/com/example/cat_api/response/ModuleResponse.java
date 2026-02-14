package com.example.cat_api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleResponse {
    private String UID;
    private String title;
    private Integer sequenceOrder;
    private Integer completionPercentage;
    private List<LessonResponse> lessons;
}
