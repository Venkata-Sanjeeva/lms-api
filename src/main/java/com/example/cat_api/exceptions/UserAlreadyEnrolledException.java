package com.example.cat_api.exceptions;

public class UserAlreadyEnrolledException extends RuntimeException {
    public UserAlreadyEnrolledException(String message) {
        super(message);
    }
}

