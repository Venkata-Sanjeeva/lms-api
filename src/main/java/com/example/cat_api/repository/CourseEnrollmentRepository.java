package com.example.cat_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cat_api.enums.CourseEnrollmentStatus;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long>{
	// Check if a user is already enrolled (matches your unique constraint)
    Optional<CourseEnrollment> findByUserAndCourse(User user, Course course);
    
    // Get all courses a specific user is enrolled in
    List<CourseEnrollment> findByUserAndStatus(User user, CourseEnrollmentStatus status);
    
    boolean existsByUserAndCourse(User user, Course course);
}
