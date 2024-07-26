package com.bjcareer.stockservice.timeDeal.service.exception;

public class RedisLockAcquisitionException extends RuntimeException{
    public RedisLockAcquisitionException(String message) {
        super(message);
    }
}