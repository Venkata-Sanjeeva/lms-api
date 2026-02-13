package com.example.cat_api.service;

import org.springframework.stereotype.Service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.response.CourseEnrollmentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

	private final CourseEnrollmentRepository courseEnrollRepo;
	private final UserService userService;
	private final CourseService courseService;
	
	public CourseEnrollmentResponse enrollUserInCourse(String userEmailId, String courseUID) throws UserNotFoundException, CourseNotFoundException {
	    // 1. Validate User and Course existence
	    User user = userService.getUserByEmail(userEmailId);
	    Course course = courseService.fetchCourseByUID(courseUID);

	    // 2. Create and Save Enrollment
	    CourseEnrollment enrollment = new CourseEnrollment();
	    enrollment.setUser(user);
	    enrollment.setCourse(course);
	    enrollment = courseEnrollRepo.save(enrollment);

	    // 3. Map to Response DTO
	    return CourseEnrollmentResponse.builder()
	    		.courseUID(courseUID)
	    		.courseTitle(course.getTitle())
	    		.userUID(user.getUserUID())
	    		.status(enrollment.getStatus())
	    		.enrolledAt(enrollment.getEnrolledAt())
	    		.build();
	}
	
}
