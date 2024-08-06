package com.bjcareer.userservice.controller.exHandler;

import com.bjcareer.userservice.controller.UserController;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.interceptor.LoginInterceptor;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice(assignableTypes={UserController.class, LoginInterceptor.class})
public class UserExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(JwtException e) {
        return buildErrorResponse("Please try logging in again.", HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwtException(ExpiredJwtException e) {
        return buildErrorResponse("Access token is expired. Please use your refresh token to get a new access token.", HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(UnauthorizedAccessAttemptException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessAttemptException(UnauthorizedAccessAttemptException e) {
        return buildErrorResponse("Unauthorized access attempt detected.", HttpStatus.UNAUTHORIZED, e);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, Exception e) {
        log.error("Error: {}", e.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}

