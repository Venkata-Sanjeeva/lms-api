package com.example.cat_api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatedLessonResponse{
    private String id;
    private String title;
    private String content;
    private String codeExample;
    private Integer sequenceOrder;
    private String moduleId;
}