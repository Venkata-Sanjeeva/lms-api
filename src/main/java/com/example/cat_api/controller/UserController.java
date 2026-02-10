package com.example.cat_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@RestController
@RequestMapping("/api/user")
public class UserController {

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
}
