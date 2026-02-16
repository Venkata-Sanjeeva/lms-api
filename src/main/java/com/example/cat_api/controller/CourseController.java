package com.example.cat_api.controller;

import com.example.cat_api.response.CoursePublicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	private final CourseService courseService;
	
	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getCourses(
    		@RequestParam(required = false) Difficulty difficulty,
    		@RequestParam(required = false) String title) {
        return ResponseEntity.status(HttpStatus.OK)
        		.body(courseService.fetchAllCourseOverviewDtos(difficulty, title));
    }
	
	@GetMapping("/{courseUID}/details")
	public ResponseEntity<?> getCourseDetails(@PathVariable String courseUID) {
		CoursePublicResponse response;
		try {
			response = courseService.fetchCourseByUIDAndConvertToDto(courseUID);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (CourseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
