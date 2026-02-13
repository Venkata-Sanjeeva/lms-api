package com.example.cat_api.service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.LessonNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.UserRepository;
import com.example.cat_api.request.UpdateProgressRequest;
import com.example.cat_api.response.CourseEnrollResponse;
import com.example.cat_api.response.LessonProgressResponse;
import com.example.cat_api.response.UserCoursesResponse;
import com.example.cat_api.response.UserEnrolledCourseResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    
    private final EnrollmentService enrollService;
    private final EnrollmentService courseEnrollService;
    private final LessonProgressService lessonProgressSevice;
    
    
    public User getUserByUID(String UID) {
    	return userRepo.findByUserUID(UID).orElseThrow(() -> new UserNotFoundException("User with " + UID + " not found!"));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

    public UserCoursesResponse fetchUserEnrolledCourseDetails(String userEmailID) throws UserNotFoundException, CourseNotFoundException {
    	User user = getUserByEmail(userEmailID);
    	String userUID = user.getUserUID();
    	
    	List<CourseEnrollment> enrolledCourses = enrollService.fetchUserEnrolledList(user);
    	
    	List<UserEnrolledCourseResponse> userEnrolledCourses = enrolledCourses.stream().map((enrollmentObj) -> {
    		Course course = enrollmentObj.getCourse();
    		Long totalEnrolledStdsInCourse = enrollService.fetchTotalEnrolledStdsCountForPartCourse(course.getCourseUID());
    		
    		return UserEnrolledCourseResponse.builder()
    				.courseUID(course.getCourseUID())
    				.courseTitle(course.getTitle())
    				.courseDesc(course.getDescription())
    				.enrolledAt(enrollmentObj.getEnrolledAt())
    				.difficulty(course.getDifficulty().toString())
    				.totalStdsEnrolled(totalEnrolledStdsInCourse)
    				.build();
    	}).toList();
    	
    	return UserCoursesResponse.builder()
    			.userUID(userUID)
    			.userEnrolledCourses(userEnrolledCourses)
    			.build();
    }
    
    public CourseEnrollResponse enrollUserInCourse(String userEmailID, String courseUID) throws UserAlreadyEnrolledException, UserNotFoundException, CourseNotFoundException {
    	User user = getUserByEmail(userEmailID);
    	return courseEnrollService.enrollUserInCourse(user, courseUID);
    }
    
    public LessonProgressResponse updateUserLessonProgress(String userEmailID, UpdateProgressRequest req) throws UserNotFoundException, LessonNotFoundException {
    	User user = getUserByEmail(userEmailID);
    	return lessonProgressSevice.updateProgress(user, req);
    }
    
    
}
