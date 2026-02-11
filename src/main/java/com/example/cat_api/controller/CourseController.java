package com.example.cat_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cat_api.enums.Difficulty;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class CourseController {

	private CourseService courseService;
	
	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getCourses(@RequestParam(required = false) Difficulty difficulty) {
        return ResponseEntity.status(HttpStatus.OK)
        		.body(courseService.fetchAllCourseOverviewDtos(difficulty));
    }
	
	@GetMapping("/{courseUniqueId}")
	public ResponseEntity<?> getCourseDetails(@PathVariable String courseUniqueId) {
		Course course;
		try {
			course = courseService.fetchCourseByUniqueId(courseUniqueId);
		} catch (CourseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(course);
	}
}
