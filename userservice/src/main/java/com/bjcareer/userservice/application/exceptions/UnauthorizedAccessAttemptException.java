package com.bjcareer.userservice.application.exceptions;

public class UnauthorizedAccessAttemptException extends RuntimeException {
    public UnauthorizedAccessAttemptException(String message) {
        super(message);
    }
}
