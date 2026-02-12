package com.example.cat_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.cat_api.exceptions.CourseAlreadyExistsException;
import com.example.cat_api.exceptions.ModuleNotFoundException;
import com.example.cat_api.request.CreateCourseRequest;
import com.example.cat_api.request.CreateLessonRequest;
import com.example.cat_api.request.CreateModuleRequest;
import com.example.cat_api.response.CreatedLessonResponse;
import com.example.cat_api.response.CreatedModuleResponse;
import com.example.cat_api.service.CourseService;
import com.example.cat_api.service.LessonService;
import com.example.cat_api.service.ModuleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {
	
	private CourseService courseService;
	private ModuleService moduleService;
	private LessonService lessonService;

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
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/courses/create")
    public ResponseEntity<?> saveCourseInDB(@RequestBody CreateCourseRequest createCourseReq) {
    	try {
    		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourseInDB(createCourseReq));
		} catch (CourseAlreadyExistsException alreadyExistsException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(alreadyExistsException.getMessage());
		} catch (Exception excep) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(excep.getMessage());
		}
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<?> addModuleToCourse(
        @PathVariable Long courseId, 
        @RequestBody CreateModuleRequest moduleReq) {
    	try {
    		CreatedModuleResponse response = moduleService.addModuleToCourse(courseId, moduleReq);
    		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (ModuleNotFoundException moduleNotFoundExcep) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(moduleNotFoundExcep.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving module!");
		}
        
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> createLesson(
            @PathVariable Long moduleId, 
            @RequestBody CreateLessonRequest request) {
    	
    	try {
			CreatedLessonResponse response = lessonService.addLessonToModule(moduleId, request);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (ModuleNotFoundException moduleNotFoundExcep) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(moduleNotFoundExcep.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving lesson!");
		}
    }
}
