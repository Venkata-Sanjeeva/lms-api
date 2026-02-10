package com.example.cat_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> adminDashboard(Authentication authentication) {
        return ResponseEntity.ok(
                "Welcome ADMIN! You are logged in as: " + authentication.getName()
        );
    }

    @PreAuthorize("hasRole('ADMIN')") // Only ROLE_ADMIN can touch this
    // To use this, you just need to add @EnableMethodSecurity to your Security Configuration class.
    @GetMapping("/manage-users")
    public ResponseEntity<?> manageUsers() {
        return ResponseEntity.ok("Only ADMIN can access this endpoint.");
    }
}
