package com.example.cat_api.exceptions;

public class UserNotEnrolledException extends RuntimeException {
	public UserNotEnrolledException(String message) {
		super(message);
	}
}
