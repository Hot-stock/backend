package com.bjcareer.gateway.exceptions;

public class UnauthorizedAccessAttemptException extends RuntimeException {
    public UnauthorizedAccessAttemptException(String message) {
        super(message);
    }
}
