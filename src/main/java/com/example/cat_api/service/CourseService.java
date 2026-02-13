package com.example.cat_api.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.cat_api.model.Lesson;
import com.example.cat_api.model.Module;
import com.example.cat_api.response.CoursePublicResponse;
import com.example.cat_api.response.LessonResponse;
import com.example.cat_api.response.ModuleResponse;
import com.example.cat_api.utils.IdentifierGenerator;
import org.springframework.stereotype.Service;

import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.exceptions.CourseAlreadyExistsException;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.repository.CourseRepository;
import com.example.cat_api.request.CreateCourseRequest;
import com.example.cat_api.response.CreatedCourseResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepo;
	private final CourseEnrollmentRepository courseEnrollRepo;
	
	private Difficulty getDifficultyFromString(String difficulty) {
        return switch (difficulty.toUpperCase()) {
            case "ADVANCED" -> Difficulty.ADVANCED;
            case "INTERMEDIATE" -> Difficulty.INTERMEDIATE;
            default -> Difficulty.BEGINNER;
        };
    }
	
	// Helper method to keep the stream clean
//	private CourseOverviewDTO convertToOverviewDto(Course course) {
//	    return new CourseOverviewDTO(
//	        course.getCourseUID(),
//	        course.getTitle(),
//	        null, // thumbnail
//	        null, // instructor
//	        course.getDifficulty(),
//	        null, // rating
//	        courseEnrollRepo.countByCourseId(course.getId()),
//	        null, // duration
//	        null  // price
//	    );
//	}
	
	public List<Course> fetchAllCourses() {
		return courseRepo.findAll();
	}
	
	public List<Course> fetchAllCoursesByDifficulty(Difficulty difficulty) {
		return courseRepo.findByDifficulty(difficulty);
	}
	
	public List<Course> fetchAllCoursesByTitle(String title) {
		return courseRepo.findByTitleContainingIgnoreCase(title);
	}
	
	public CoursePublicResponse fetchCourseByUIDAndConvertToDto(String uid) throws CourseNotFoundException {
		return mapToPublicResponse(fetchCourseByUID(uid));
	}
	
	public Course fetchCourseByUID(String uid) {
		Optional<Course> courseOpt = courseRepo.findByCourseUID(uid);
		
		if(courseOpt.isPresent()) {
			return courseOpt.get();
		}

		throw new CourseNotFoundException("Course not found with ID: " + uid);
	}
	
	public List<CoursePublicResponse> fetchAllCourseOverviewDtos(Difficulty difficulty, String title) {
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
	            .map(this::mapToPublicResponse)
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
		
		newCourse.setCourseUID(IdentifierGenerator.generate("CRS"));
		newCourse.setTitle(courseTitle);
		newCourse.setDescription(courseDesc);
		newCourse.setLanguage(courseLang);
		newCourse.setDifficulty(courseDiff);
		
		Course savedCourse = courseRepo.save(newCourse);
		
		return new CreatedCourseResponse(
					savedCourse.getCourseUID(),
					savedCourse.getTitle(),
					savedCourse.getDescription(),
					savedCourse.getDifficulty().toString(),
					savedCourse.getLanguage(),
					savedCourse.getCreatedAt()
				);
	}

	private CoursePublicResponse mapToPublicResponse(Course course) {
		return CoursePublicResponse.builder()
				.courseUID(course.getCourseUID())
				.title(course.getTitle())
				.description(course.getDescription())
				.difficulty(course.getDifficulty().toString())
				.language(course.getLanguage())
				.enrollStudentsCount(courseEnrollRepo.countByCourseId(course.getId()))
				.modules(course.getModuleList().stream()
						.sorted(Comparator.comparing(Module::getSequenceOrder))
						.map(module -> ModuleResponse.builder()
								.id(module.getId())
								.title(module.getTitle())
								.sequenceOrder(module.getSequenceOrder())
								.lessons(module.getLessons().stream()
										.sorted(Comparator.comparing(Lesson::getSequenceOrder))
										.map(lesson -> LessonResponse.builder()
												.id(lesson.getId())
												.title(lesson.getTitle())
												.sequenceOrder(lesson.getSequenceOrder())
												.build())
										.toList())
								.build())
						.toList())
				.build();
	}
	
}
