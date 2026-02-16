package com.example.cat_api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    private String UID;
    private String title;
    private String content;
    private String codeExample;
    private Integer sequenceOrder;
    private Boolean isCompleted;
}