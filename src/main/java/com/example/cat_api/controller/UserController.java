package com.example.cat_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

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
