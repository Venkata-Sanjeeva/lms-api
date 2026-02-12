package com.example.cat_api.exceptions;

public class ModuleNotFoundException extends RuntimeException {
	public ModuleNotFoundException(String message) {
		super(message);
	}
}
