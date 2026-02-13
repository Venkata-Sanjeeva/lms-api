package com.example.cat_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.response.CourseEnrollmentResponse;
import com.example.cat_api.service.EnrollmentService;

@RestController
@RequestMapping("/api/user/enroll")
@CrossOrigin("*")
public class EnrollmentController {

	private EnrollmentService enrollService;
	
	public EnrollmentController(EnrollmentService enrollService) {
		this.enrollService = enrollService;
	}
	
	@PostMapping("/{courseUID}")
	public ResponseEntity<?> enrollUser(
			@PathVariable String courseUID,
			Authentication authentication) {
		try {
			CourseEnrollmentResponse response = enrollService.enrollUserInCourse(authentication.getName(), courseUID);
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (UserNotFoundException userNotFoundExcep) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundExcep.getMessage());
		} catch (CourseNotFoundException courseNotFoundExcep) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(courseNotFoundExcep.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + "\nError enrolling user!");
		}
	}
}
