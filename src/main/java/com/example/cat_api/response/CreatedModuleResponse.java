package com.example.cat_api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedModuleResponse {
	private Long moduleId;
    private String title;
    private Integer sequenceOrder;
    private String courseUniqueId; // Useful for UI breadcrumbs
    private int lessonCount;       // Nice to have for the dashboard
}