package com.example.cat_api.response;

import com.example.cat_api.enums.CourseEnrollmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CourseEnrollResponse {

    private String enrollmentId;   // you can send Long also
    private CourseEnrollmentStatus status;
    private LocalDateTime enrolledAt;

    private String userId;
    private String userName;
    private String userEmail;

    private String courseId;
    private String courseTitle;
}
