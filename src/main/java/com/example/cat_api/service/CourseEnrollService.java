package com.example.cat_api.service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.CourseEnrollment;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.CourseEnrollmentRepository;
import com.example.cat_api.response.CourseEnrollResponse;
import com.example.cat_api.utils.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseEnrollService {

    private final CourseEnrollmentRepository courseEnrollRepo;
    private final UserService userService;
    private final CourseService courseService;

    public CourseEnrollResponse enrollUserInSelectedCourse(String userEmailId, String courseUniqueId) throws UserNotFoundException,
            CourseNotFoundException,
            UserAlreadyEnrolledException {
        User user = userService.getUserByEmail(userEmailId);
        Course course = courseService.fetchCourseByUniqueId(courseUniqueId);

        courseEnrollRepo.findByUserAndCourse(user, course)
                .ifPresent(enrollment -> {
                    throw new UserAlreadyEnrolledException(
                            "User is already enrolled in course: " + courseUniqueId
                    );
                });

        CourseEnrollment courseEnrollment = new CourseEnrollment();

        courseEnrollment.setUser(user);
        courseEnrollment.setCourse(course);
        courseEnrollment.setCourseEnrollUniqueId(IdentifierGenerator.generate("ENR"));

        CourseEnrollment savedCourseEnroll = courseEnrollRepo.save(courseEnrollment);

        return CourseEnrollResponse.builder()
                .courseId(courseUniqueId)
                .courseTitle(course.getTitle())
                .enrollmentId(savedCourseEnroll.getCourseEnrollUniqueId())
                .enrolledAt(savedCourseEnroll.getEnrolledAt())
                .status(savedCourseEnroll.getStatus())
                .userId(user.getUserUniqueId())
                .userName(user.getName())
                .userEmail(userEmailId)
                .build();
    }

}
