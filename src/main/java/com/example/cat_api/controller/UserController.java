package com.example.cat_api.controller;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.LessonNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.request.UpdateProgressRequest;
import com.example.cat_api.response.CourseEnrollResponse;
import com.example.cat_api.response.LessonProgressResponse;
import com.example.cat_api.response.UserCoursesResponse;
import com.example.cat_api.response.UserEnrolledCourseResponse;
import com.example.cat_api.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

	@Operation(summary = "Get user profile", 
	           description = "Requires a valid JWT in the Authorization header")
	@Parameter(name = "Authorization", 
	           description = "JSON Web Token (JWT) for authentication", 
	           required = true, 
	           in = ParameterIn.HEADER)
    @GetMapping("/profile")
    public ResponseEntity<?> userProfile(Authentication authentication) {

        return ResponseEntity.ok(
                "Welcome USER! You are logged in as: " + authentication.getName()
        );
    }

    @GetMapping("/courses")
    public ResponseEntity<?> userCourses(Authentication authentication) {
        try {
			UserCoursesResponse response = userService.fetchUserEnrolledCourses(authentication.getName());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (UserNotFoundException | CourseNotFoundException notFoundExcept) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundExcept.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }
    
    @GetMapping("/courses/{courseUID}")
    public ResponseEntity<?> userEnrolledCourseDetails(@PathVariable String courseUID, Authentication auth) {
    	try {
			UserEnrolledCourseResponse response = userService.fetchUserEnrolledCourseDetails(auth.getName(), courseUID);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (UserNotFoundException | CourseNotFoundException notFoundExcept) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundExcept.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/courses/{courseUID}/enroll")
    public ResponseEntity<?> enrollUserInCourse(
            @PathVariable String courseUID,
            Authentication authentication
    ) {
        try {
            CourseEnrollResponse response = userService.enrollUserInCourse(authentication.getName(), courseUID);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyEnrolledException alreadyEnrolledExcept) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(alreadyEnrolledExcept.getMessage());
        } catch (UserNotFoundException | CourseNotFoundException notException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @PostMapping("/progress/update")
    public ResponseEntity<?> updateLessonProgress(@RequestBody UpdateProgressRequest request, Authentication authentication) {
    	try {
    		LessonProgressResponse response = userService.updateUserLessonProgress(authentication.getName(), request);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (UserNotFoundException | LessonNotFoundException notFoundExcept) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundExcept.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
    }
    
    
}
