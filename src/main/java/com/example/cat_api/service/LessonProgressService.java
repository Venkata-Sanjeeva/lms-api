package com.example.cat_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.LessonNotFoundException;
import com.example.cat_api.exceptions.UserNotEnrolledException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.LessonProgress;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.LessonProgressRepository;
import com.example.cat_api.request.UpdateProgressRequest;
import com.example.cat_api.response.LessonProgressResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonProgressService {
	
	private final LessonService lessonService;
	private final LessonProgressRepository lessonProgressRepo;
	private final EnrollmentService enrollService;

    public LessonProgressResponse updateProgress(User user, UpdateProgressRequest request) throws UserNotFoundException, CourseNotFoundException, LessonNotFoundException, UserNotEnrolledException {
    	
    	if (!enrollService.existsByUserUIDAndCourseUID(user.getUserUID(), request.getCourseUID())) {
    	    throw new UserNotEnrolledException("User with email ID: " + user.getEmail() + " not enrolled in course with ID: " + request.getCourseUID());
    	}
    	
    	LessonProgress progress = lessonProgressRepo.findByUser_UserUIDAndLesson_LessonUID(user.getUserUID(), request.getLessonUID())
    			.orElseGet(() -> {
			        // ONLY fetch these if we are creating a NEW progress record
			        LessonProgress newProgress = new LessonProgress();
			        newProgress.setUser(user);
			        newProgress.setLesson(lessonService.fetchLessonByUID(request.getLessonUID()));
			        return newProgress;
    			});

		progress.setCompleted(request.isCompleted());
		progress.setCompletedAt(request.isCompleted() ? LocalDateTime.now() : null);

        LessonProgress saved = lessonProgressRepo.save(progress);

        return LessonProgressResponse.builder()
                .id(saved.getId())
                .lessonUID(saved.getLesson().getLessonUID())
                .lessonTitle(saved.getLesson().getTitle())
                .isCompleted(saved.isCompleted())
                .completedAt(saved.getCompletedAt())
                .build();
    }
}