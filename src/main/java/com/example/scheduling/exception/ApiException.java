package com.example.scheduling.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
