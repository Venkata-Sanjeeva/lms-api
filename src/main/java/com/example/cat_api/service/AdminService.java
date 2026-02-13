package com.example.cat_api.service;

import org.springframework.stereotype.Service;

import com.example.cat_api.exceptions.CourseAlreadyExistsException;
import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.exceptions.ModuleNotFoundException;
import com.example.cat_api.request.CreateCourseRequest;
import com.example.cat_api.request.CreateLessonRequest;
import com.example.cat_api.request.CreateModuleRequest;
import com.example.cat_api.response.CreatedCourseResponse;
import com.example.cat_api.response.CreatedLessonResponse;
import com.example.cat_api.response.CreatedModuleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final CourseService courseSerivce;
	private final ModuleService moduleService;
	private final LessonService lessonService;
	
	public CreatedCourseResponse createCourse(CreateCourseRequest req) throws CourseAlreadyExistsException {
		return courseSerivce.saveCourseInDB(req);
	}
	
	public CreatedModuleResponse createModule(String courseId, CreateModuleRequest request) throws CourseNotFoundException {
		return moduleService.addModuleToCourse(courseId, request);
	}
	
	public CreatedLessonResponse createLesson(String moduleUID, CreateLessonRequest request) throws ModuleNotFoundException {
		return lessonService.addLessonToModule(moduleUID, request);
	}
	
	
}
