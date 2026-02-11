package com.example.cat_api.exceptions;

public class CourseNotFoundException extends RuntimeException {
	public CourseNotFoundException(String message) {
		super(message);
	}
}
