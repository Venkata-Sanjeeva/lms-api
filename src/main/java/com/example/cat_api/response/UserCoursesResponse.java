package com.example.cat_api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoursesResponse {
	private String userUID;
	private List<EnrolledCourseResponse> userEnrolledCourses;
}
