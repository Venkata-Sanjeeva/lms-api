package com.example.cat_api.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEnrolledCourseResponse {
	private String courseUID;
	private String courseTitle;
	private String courseDesc;
	private LocalDateTime enrolledAt;
	private String difficulty;
	private Long totalStdsEnrolled;
}
