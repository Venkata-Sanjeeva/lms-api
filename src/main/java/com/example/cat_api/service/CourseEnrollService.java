package com.example.cat_api.service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.response.CourseEnrollResponse;
import com.example.cat_api.utils.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseEnrollService {

    private final CourseEnrollmentRepository courseEnrollRepo;
    private final UserService userService;
    private final CourseService courseService;

    public CourseEnrollResponse enrollUserInSelectedCourse(String userEmailId, String courseUID) throws UserNotFoundException,
            CourseNotFoundException,
            UserAlreadyEnrolledException {
        User user = userService.getUserByEmail(userEmailId);
        Course course = courseService.fetchCourseByUID(courseUID);

        courseEnrollRepo.findByUserAndCourse(user, course)
                .ifPresent(enrollment -> {
                    throw new UserAlreadyEnrolledException(
                            "User is already enrolled in course: " + courseUID
                    );
                });

        CourseEnrollment courseEnrollment = new CourseEnrollment();

        courseEnrollment.setUser(user);
        courseEnrollment.setCourse(course);
        courseEnrollment.setCourseEnrollUID(IdentifierGenerator.generate("ENR"));

        CourseEnrollment savedCourseEnroll = courseEnrollRepo.save(courseEnrollment);

        return CourseEnrollResponse.builder()
                .courseId(courseUID)
                .courseTitle(course.getTitle())
                .enrollmentId(savedCourseEnroll.getCourseEnrollUID())
                .enrolledAt(savedCourseEnroll.getEnrolledAt())
                .status(savedCourseEnroll.getStatus())
                .userId(user.getUserUID())
                .userName(user.getName())
                .userEmail(userEmailId)
                .build();
    }

    public CourseEnrollment fetchByUserUIDandCourseUID(String userUID, String courseUID) throws UserNotFoundException, CourseNotFoundException, UserNotEnrolledException {
    	return courseEnrollRepo
    			.findByUser_UserUIDAndCourse_CourseUID(userUID, courseUID)
    			.orElseThrow(() -> new UserNotEnrolledException("User with ID: " + userUID + " not enrolled in course with ID: " + courseUID));
    }
    
    public boolean existsByUserUIDAndCourseUID(String userUID, String courseUID) {
    	return courseEnrollRepo.existsByUser_UserUIDAndCourse_CourseUID(userUID, courseUID);
    }
}
