package com.example.cat_api.exceptions;

public class CourseAlreadyExistsException extends RuntimeException {
	public CourseAlreadyExistsException(String message) {
		super(message);
	}
}
