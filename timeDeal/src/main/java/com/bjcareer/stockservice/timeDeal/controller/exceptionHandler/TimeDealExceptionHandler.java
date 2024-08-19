package com.bjcareer.stockservice.timeDeal.controller.exceptionHandler;

import com.bjcareer.stockservice.timeDeal.controller.TimeDealController;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.CouponLimitExceededException;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.DuplicateParticipationException;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {TimeDealController.class})
public class TimeDealExceptionHandler {

    @ExceptionHandler(RedisLockAcquisitionException.class)
    public ResponseEntity<ErrorResult> handleRedisLockException(RedisLockAcquisitionException e) {
        log.error("Redis lock acquisition failed: {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult("The server is unstable. Please try again later.");
        return new ResponseEntity<>(errorResult, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CouponLimitExceededException.class)
    public ResponseEntity<ErrorResult> handleCouponLimitExceededException(CouponLimitExceededException e) {
        log.error("Coupon limit exceeded: {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult("The coupon issuance limit has been exceeded.");
        return new ResponseEntity<>(errorResult, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<ErrorResult> handleInvalidEventException(InvalidEventException e) {
        log.error("Invalid event ID: {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult("Invalid event ID. Please check your request.");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateParticipationException.class)
    public ResponseEntity<ErrorResult> handleDuplicateParticipationException(DuplicateParticipationException e) {
        log.error("Duplicate Participation{}", e.getMessage());
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleGenericException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult("The server is unstable. Please try again later.");
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
