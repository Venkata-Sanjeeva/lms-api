package com.example.cat_api.dto;

import java.time.LocalDateTime;

import com.example.cat_api.enums.CourseEnrollmentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentDto {

	private String courseUniqueId;
    private String courseTitle;
    private String userUniqueId;
    private CourseEnrollmentStatus status;
    private LocalDateTime enrolledAt;
    
}
