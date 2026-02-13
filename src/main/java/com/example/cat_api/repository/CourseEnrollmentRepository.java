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

    Optional<CourseEnrollment> findByUser_UserUIDAndCourse_CourseUID(String userUID, String courseUID);

    // Get all courses a specific user is enrolled in
    List<CourseEnrollment> findByUserAndStatus(User user, CourseEnrollmentStatus status);
    
    // Option A: Get the full list (use this if you need student details)
    List<CourseEnrollment> findByCourseId(Long courseId);

    // Option B: Get just the count (much faster for an "Overview" view)
    long countByCourseId(Long courseId);
    
    boolean existsByUserAndCourse(User user, Course course);
    
 // Inside CourseEnrollmentRepository
    boolean existsByUser_UserUIDAndCourse_CourseUID(String userUID, String courseUID);
}
