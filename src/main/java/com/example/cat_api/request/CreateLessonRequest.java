package com.example.cat_api.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateLessonRequest {
    private String title;
    private String content;
    private String codeExample;
    private Integer sequenceOrder;
    private List<ResourceRequest> resourcesList;
}