package com.example.cat_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cat_api.dto.CourseOverviewDTO;
import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.repository.CourseRepository;

@Service
public class CourseService {

	private CourseRepository courseRepo;
	private CourseEnrollmentRepository courseEnrollRepo;
	
	public CourseService(
			CourseRepository courseRepo,
			CourseEnrollmentRepository courseEnrollRepo
			) {
		this.courseRepo = courseRepo;
		this.courseEnrollRepo = courseEnrollRepo;
	}
	
	// Helper method to keep the stream clean
	private CourseOverviewDTO convertToOverviewDto(Course course) {
	    return new CourseOverviewDTO(
	        course.getCourseUniqueId(),
	        course.getTitle(),
	        null, // thumbnail
	        null, // instructor
	        course.getDifficulty(),
	        null, // rating
	        courseEnrollRepo.countByCourseId(course.getId()),
	        null, // duration
	        null  // price
	    );
	}
	
	public List<Course> fetchAllCourses() {
		return courseRepo.findAll();
	}
	
	public List<Course> fetchAllCoursesByDifficulty(Difficulty difficulty) {
		return courseRepo.findByDifficulty(difficulty);
	}
	
	public List<Course> fetchAllCoursesByTitle(String title) {
		return courseRepo.findByTitleContainingIgnoreCase(title);
	}
	
	public CourseOverviewDTO fetchCourseByUniqueId(String uniqueId) throws CourseNotFoundException {
		Optional<Course> courseOpt = courseRepo.findByCourseUniqueId(uniqueId);
		
		if(courseOpt.isPresent()) {
			return convertToOverviewDto(courseOpt.get());
		}
		
		throw new CourseNotFoundException("Course not found with ID: " + uniqueId);
	}
	
	public List<CourseOverviewDTO> fetchAllCourseOverviewDtos(Difficulty difficulty, String title) {
	    List<Course> courses;

	    // Logic to determine which repository method to call
	    if (title != null && !title.isEmpty()) {
	        courses = fetchAllCoursesByTitle(title);
	    } else if (difficulty != null) {
	        courses = fetchAllCoursesByDifficulty(difficulty);
	    } else {
	        courses = fetchAllCourses();
	    }

	    // Centralized mapping logic
	    return courses.stream()
	            .map(this::convertToOverviewDto)
	            .collect(Collectors.toList());
	}	
	
}
