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
	
	public List<CourseOverviewDTO> fetchAllCourseOverviewDtos(Difficulty difficulty) {
		List<Course> courses = (difficulty == null) 
		        ? fetchAllCourses() 
		        : fetchAllCoursesByDifficulty(difficulty);
		
		List<CourseOverviewDTO> overviewList = courses.stream()
		        .map(course -> new CourseOverviewDTO(
		            course.getCourseUniqueId(),
		            course.getTitle(),
		            null,
		            null,
		            course.getDifficulty(),
		            null,
		            courseEnrollRepo.countByCourseId(course.getId()),
		            null,
		            null
		        ))
		        .collect(Collectors.toList());
		
		return overviewList;
	}
	
	public List<Course> fetchAllCourses() {
		return courseRepo.findAll();
	}
	
	public List<Course> fetchAllCoursesByDifficulty(Difficulty difficulty) {
		return courseRepo.findByDifficulty(difficulty);
	}
	
	public Course fetchCourseByUniqueId(String uniqueId) throws CourseNotFoundException {
		Optional<Course> courseOpt = courseRepo.findByCourseUniqueId(uniqueId);
		
		if(courseOpt.isPresent()) {
			Course course = courseOpt.get();
			
			CourseOverviewDTO courseDto = new CourseOverviewDTO();
			
			courseDto.setUniqueId(course.getCourseUniqueId());
			courseDto.setTitle(course.getTitle());
			courseDto.setDifficulty(course.getDifficulty());
			courseDto.setDuration(null);
			courseDto.setInstructorName(null);
			courseDto.setPrice(null);
			courseDto.setRating(null);
			courseDto.setThumbnailUri(null);
			courseDto.setTotalStudents(courseEnrollRepo.countByCourseId(course.getId()));
			
		}
		
		throw new CourseNotFoundException("Course not found with ID: " + uniqueId);
	}
}
