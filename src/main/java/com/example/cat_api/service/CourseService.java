package com.example.cat_api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cat_api.dto.CourseOverviewDTO;
import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.exceptions.CourseAlreadyExistsException;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.repository.CourseRepository;
import com.example.cat_api.request.CreateCourseRequest;
import com.example.cat_api.response.CreatedCourseResponse;

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
	
	private Difficulty getDifficultyFromString(String difficulty) {
        return switch (difficulty.toUpperCase()) {
            case "ADVANCED" -> Difficulty.ADVANCED;
            case "INTERMEDIATE" -> Difficulty.INTERMEDIATE;
            default -> Difficulty.BEGINNER;
        };
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
	
	public CourseOverviewDTO fetchCourseByUniqueIdAndConvertToDto(String uniqueId) throws CourseNotFoundException {
		return convertToOverviewDto(fetchCourseByUniqueId(uniqueId));
	}
	
	public Course fetchCourseByUniqueId(String uniqueId) {
		Optional<Course> courseOpt = courseRepo.findByCourseUniqueId(uniqueId);
		
		if(courseOpt.isPresent()) {
			return courseOpt.get();
		}
		
		throw new CourseNotFoundException("Course not found with ID: " + uniqueId);
	}
	
	public List<CourseOverviewDTO> fetchAllCourseOverviewDtos(Difficulty difficulty, String title) {
	    List<Course> courses;

	    boolean hasTitle = (title != null && !title.isEmpty());
	    boolean hasDifficulty = (difficulty != null);

	    // 1. Both filters present
	    if (hasTitle && hasDifficulty) {
	        courses = courseRepo.findByDifficultyAndTitleContainingIgnoreCase(difficulty, title);
	    } 
	    // 2. Only title present
	    else if (hasTitle) {
	        courses = fetchAllCoursesByTitle(title);
	    } 
	    // 3. Only difficulty present
	    else if (hasDifficulty) {
	        courses = fetchAllCoursesByDifficulty(difficulty);
	    } 
	    // 4. No filters
	    else {
	        courses = fetchAllCourses();
	    }

	    return courses.stream()
	            .map(this::convertToOverviewDto)
	            .collect(Collectors.toList());
	}
	
	public CreatedCourseResponse saveCourseInDB(CreateCourseRequest courseReq) {
		
		String courseTitle = courseReq.getTitle();
		String courseDesc = courseReq.getDescription();
		String courseLang = courseReq.getLanguage();
		Difficulty courseDiff = getDifficultyFromString(courseReq.getDifficulty());
		
		if(courseRepo.existsByDifficultyAndTitleContainingIgnoreCase(courseDiff, courseTitle)) {
			throw new CourseAlreadyExistsException("Course with title: " + courseTitle + ", already exists in DB.");
		}

		Course newCourse = new Course();
		
		newCourse.setCourseUniqueId("COURSE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		newCourse.setTitle(courseTitle);
		newCourse.setDescription(courseDesc);
		newCourse.setLanguage(courseLang);
		newCourse.setDifficulty(courseDiff);
		
		Course savedCourse = courseRepo.save(newCourse);
		
		return new CreatedCourseResponse(
					savedCourse.getCourseUniqueId(),
					savedCourse.getTitle(),
					savedCourse.getDescription(),
					savedCourse.getDifficulty().toString(),
					savedCourse.getLanguage(),
					savedCourse.getCreatedAt()
				);
	}
	
}
