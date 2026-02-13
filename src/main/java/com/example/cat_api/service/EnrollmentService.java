package com.example.cat_api.service;

import org.springframework.stereotype.Service;

import com.example.cat_api.dto.CourseEnrollmentDto;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.CourseEnrollmentRepository;

@Service
public class EnrollmentService {

	private CourseEnrollmentRepository courseEnrollRepo;
	private UserService userService;
	private CourseService courseService;
	
	public EnrollmentService(
			CourseEnrollmentRepository courseEnrollRepo, 
			UserService userService, 
			CourseService courseService) {
		this.userService = userService;
		this.courseService = courseService;
		this.courseEnrollRepo = courseEnrollRepo;
	}
	
	public CourseEnrollmentDto enrollUserInCourse(String userEmailId, String courseUID) throws UserNotFoundException, CourseNotFoundException {
	    // 1. Validate User and Course existence
	    User user = userService.getUserByEmail(userEmailId);
	    Course course = courseService.fetchCourseByUID(courseUID);

	    // 2. Create and Save Enrollment
	    CourseEnrollment enrollment = new CourseEnrollment();
	    enrollment.setUser(user);
	    enrollment.setCourse(course);
	    enrollment = courseEnrollRepo.save(enrollment);

	    // 3. Map to Response DTO
	    return new CourseEnrollmentDto(
	        course.getCourseUID(),
	        course.getTitle(),
	        user.getUserUID(),
	        enrollment.getStatus(),
	        enrollment.getEnrolledAt()
	    );
	}
	
}
