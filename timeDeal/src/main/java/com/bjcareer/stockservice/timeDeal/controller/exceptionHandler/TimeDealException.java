package com.bjcareer.stockservice.timeDeal.controller.exceptionHandler;


import com.bjcareer.stockservice.timeDeal.service.TimeDealService;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {TimeDealService.class})
public class TimeDealException {

    @ExceptionHandler
    public ResponseEntity RedisLockExHandler(RedisLockAcquisitionException e){
        log.error(e.getMessage());
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity illegalArgumentExHandler(IllegalArgumentException e){
        log.error(e.getMessage());
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }
}
