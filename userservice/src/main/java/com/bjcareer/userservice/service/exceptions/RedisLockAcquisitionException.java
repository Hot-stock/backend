package com.bjcareer.userservice.service.exceptions;

public class RedisLockAcquisitionException extends RuntimeException{
    public RedisLockAcquisitionException() {
        super();
    }

    public RedisLockAcquisitionException(String message) {
        super(message);
    }

    public RedisLockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisLockAcquisitionException(Throwable cause) {
        super(cause);
    }

    protected RedisLockAcquisitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
