package com.bjcareer.userservice.application.token.exceptions;

public class UnauthorizedAccessAttemptException extends RuntimeException {
    public UnauthorizedAccessAttemptException(String message) {
        super(message);
    }
}
