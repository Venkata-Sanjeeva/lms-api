package com.example.cat_api.controller;

import com.example.cat_api.exceptions.EmailAlreadyExistsException;
import com.example.cat_api.exceptions.InvalidLoginCredentialsException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.request.LoginRequest;
import com.example.cat_api.request.RegisterRequest;
import com.example.cat_api.response.AuthResponse;
import com.example.cat_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/{role}/register")
    public ResponseEntity<?> register(
    		@RequestBody RegisterRequest request,
    		@PathVariable String role) {
        try {
            AuthResponse authRes = authService.register(request, role);
            return ResponseEntity.status(HttpStatus.CREATED).body(authRes);
        } catch (EmailAlreadyExistsException existsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while registering...");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authRes = authService.login(request);
            return ResponseEntity.ok(authRes);
        } catch (InvalidLoginCredentialsException credentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials (wrong email/password)");
        } catch (UserNotFoundException userNotFoundExcep) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundExcep.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while verifying credentials!");
        }
    }
}
