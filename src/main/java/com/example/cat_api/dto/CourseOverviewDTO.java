package com.example.cat_api.dto;

import com.example.cat_api.enums.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseOverviewDTO {
    private String uniqueId;
    private String title;
    private String thumbnailUri;
    private String instructorName;
    private Difficulty difficulty;
    private Double rating;
    private Long totalStudents;
    private String duration; // e.g., "12h 30m"
    private Double price;
}

/*

  {
    "id": 101,
    "title": "Java Masterclass",
    "instructorName": "Jane Doe",
    "difficulty": "INTERMEDIATE",
    "rating": 4.8,
    "totalStudents": 1250,
    "duration": "10h",
    "price": 49.99
  }

*/