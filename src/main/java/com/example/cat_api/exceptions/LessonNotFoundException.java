package com.example.cat_api.exceptions;

public class LessonNotFoundException extends RuntimeException {
	public LessonNotFoundException(String message) {
		super(message);
	}
}
