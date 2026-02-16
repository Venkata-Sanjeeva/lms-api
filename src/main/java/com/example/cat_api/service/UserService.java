package com.example.cat_api.service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.LessonNotFoundException;
import com.example.cat_api.exceptions.UserAlreadyEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.*;
import com.example.cat_api.model.Module;
import com.example.cat_api.repository.UserRepository;
import com.example.cat_api.request.UpdateProgressRequest;
import com.example.cat_api.response.*;

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

	public Integer calculateCourseProgress(String userUID, Course course) {
		List<Module> moduleList = course.getModuleList();
		if (moduleList == null || moduleList.isEmpty()) return 0;

		int totalCourseLessons = 0;
		double weightedSum = 0;

		for (Module module : moduleList) {
			int moduleLessonCount = module.getLessons().size();
			if (moduleLessonCount == 0) continue;

			// Reuse your existing module progress logic
			int modulePercentage = calculateModuleProgress(userUID, module);

			// Add to weighted sum: (Percentage * Weight)
			weightedSum += (modulePercentage * moduleLessonCount);
			totalCourseLessons += moduleLessonCount;
		}

		if (totalCourseLessons == 0) return 0;

		return (int) (weightedSum / totalCourseLessons);
	}

	public Integer calculateModuleProgress(String userUID, Module module) {
		List<Lesson> lessonList = module.getLessons();
		int totalLessons = lessonList.size();
		int completedLessons = 0;

		for (Lesson lesson : lessonList) {
			LessonProgress progress = lessonProgressSevice.fetchLessonProgressByUserUIDandLessonUID(userUID, lesson.getLessonUID());
			if (progress != null && progress.isCompleted()) {
				completedLessons++;
			}
		}

		// Calculate percentage for THIS specific module
		int percentage = 0;
		if (totalLessons > 0) {
			percentage = (int) (((double) completedLessons / totalLessons) * 100);
		}
		return percentage;
	}
	
	public UserEnrolledCourseResponse convertEnrolledCourseToResponse(String userUID, CourseEnrollment enrolledCourse) throws CourseNotFoundException {
		Course course = enrolledCourse.getCourse();
		Long totalEnrolledStdsInCourse = enrollService.fetchTotalEnrolledStdsCountForPartCourse(course.getCourseUID());
		
		return UserEnrolledCourseResponse.builder()
				.courseUID(course.getCourseUID())
				.courseTitle(course.getTitle())
				.courseDesc(course.getDescription())
				.enrolledAt(enrolledCourse.getEnrolledAt())
				.difficulty(course.getDifficulty().toString())
				.totalStdsEnrolled(totalEnrolledStdsInCourse)
				.completionPercentage(calculateCourseProgress(userUID, course))
				.modulesList(course.getModuleList()
						.stream().map((moduleObj) -> ModuleResponse.builder()
								.UID(moduleObj.getModuleUID())
								.title(moduleObj.getTitle())
								.sequenceOrder(moduleObj.getSequenceOrder())
								.completionPercentage(calculateModuleProgress(userUID, moduleObj))
								.lessons(moduleObj.getLessons()
										.stream()
										.map((lessonObj) -> LessonResponse.builder()
												.UID(lessonObj.getLessonUID())
												.title(lessonObj.getTitle())
												.content(lessonObj.getContent())
												.codeExample(lessonObj.getCodeExample())
												.sequenceOrder(lessonObj.getSequenceOrder())
												.isCompleted(lessonProgressSevice.existsByUserUIDandLessonUID(userUID, lessonObj.getLessonUID()))
												.build())
										.toList())
								.build())
						.toList())
				.build();
	}
	
	public UserEnrolledCourseResponse fetchUserEnrolledCourseDetails(String userEmailID, String courseUID) throws UserNotFoundException, CourseNotFoundException {
		User user = getUserByEmail(userEmailID);
    	String userUID = user.getUserUID();
    	
		CourseEnrollment enrolledCourse = enrollService.fetchByUserUIDandCourseUID(userUID, courseUID);
		
		return convertEnrolledCourseToResponse(userUID, enrolledCourse);
	}

    public UserCoursesResponse fetchUserEnrolledCourses(String userEmailID) throws UserNotFoundException, CourseNotFoundException {
    	User user = getUserByEmail(userEmailID);
    	String userUID = user.getUserUID();
    	
    	List<CourseEnrollment> enrolledCourses = enrollService.fetchUserEnrolledList(user);
    	
    	List<EnrolledCourseResponse> userEnrolledCourses = enrolledCourses
				.stream()
				.map((enrollmentObj) -> {
					Course course = enrollmentObj.getCourse();
						Long totalEnrolledStdsInCourse = enrollService.fetchTotalEnrolledStdsCountForPartCourse(course.getCourseUID());
						return EnrolledCourseResponse.builder()
								.courseUID(course.getCourseUID())
								.courseTitle(course.getTitle())
								.courseDesc(course.getDescription())
								.enrolledAt(enrollmentObj.getEnrolledAt())
								.difficulty(course.getDifficulty().toString())
								.totalStdsEnrolled(totalEnrolledStdsInCourse)
								.completionPercentage(calculateCourseProgress(userUID, course))
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
