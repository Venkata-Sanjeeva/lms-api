package com.example.cat_api.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCourseResponse{
    private String courseUID;
    private String title;
    private String description;
    private String difficulty;
    private String language;
    private LocalDateTime createdAt;
    // You can add 'moduleCount' here later to show how many modules it has!
}
