package com.example.cat_api.response;

import java.time.LocalDateTime;

import com.example.cat_api.enums.CourseEnrollmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentResponse{

	private String courseUID;
    private String courseTitle;
    private String userUID;
    private CourseEnrollmentStatus status;
    private LocalDateTime enrolledAt;
    
}
