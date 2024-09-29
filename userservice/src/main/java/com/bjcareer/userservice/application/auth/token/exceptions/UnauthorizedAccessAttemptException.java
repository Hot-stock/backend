package com.bjcareer.userservice.application.auth.token.exceptions;

public class UnauthorizedAccessAttemptException extends RuntimeException {
    public UnauthorizedAccessAttemptException(String message) {
        super(message);
    }
}
