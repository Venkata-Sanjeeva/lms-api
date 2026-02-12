package com.example.cat_api.controller;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.response.CourseEnrollResponse;
import com.example.cat_api.service.CourseEnrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final CourseEnrollService courseEnrollService;

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
    public ResponseEntity<?> userCourses() {
        return ResponseEntity.ok("These are your enrolled courses (USER endpoint).");
    }

    @PostMapping("/courses/{courseUniqueId}/enroll")
    public ResponseEntity<?> enrollUserInCourse(
            @PathVariable String courseUniqueId,
            Authentication authentication
    ) {
        try {
            CourseEnrollResponse response = courseEnrollService.enrollUserInSelectedCourse(authentication.getName(), courseUniqueId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyEnrolledException alreadyEnrolledExcept) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(alreadyEnrolledExcept.getMessage());
        } catch (UserNotFoundException | CourseNotFoundException notException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
