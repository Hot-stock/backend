package com.bjcareer.userservice.out.persistance.repository.exceptions;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseOperationException(String message) {
        super(message);
    }
}