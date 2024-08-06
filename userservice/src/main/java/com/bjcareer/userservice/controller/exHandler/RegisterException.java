package com.bjcareer.userservice.controller.exHandler;


import com.bjcareer.userservice.controller.RegisterController;
import com.bjcareer.userservice.service.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.service.exceptions.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes={RegisterController.class})
public class RegisterException {

    @ExceptionHandler
    public ResponseEntity<?> redisLockAcquisitionException(RedisLockAcquisitionException e) {
        log.error(e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<?> userAl(UserAlreadyExistException e) {
        log.error(e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

}
