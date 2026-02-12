package com.example.cat_api.request;

import lombok.Data;

@Data
public class CreateLessonRequest {
    private String title;
    private String content;
    private String codeExample;
    private Integer sequenceOrder; 
}