package com.bjcareer.gateway.aop.JWT;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.bjcareer.gateway.aop.CommonAOP;
import com.bjcareer.gateway.common.JWTUtil;
import com.bjcareer.gateway.common.TokenValidationResult;
import com.bjcareer.gateway.exceptions.UnauthorizedAccessAttemptException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTLoginAspect {
	private final JWTUtil jwtUtil;

	@Pointcut("@annotation(com.bjcareer.gateway.aop.JWT.JWTLogin)")
	private void cut() {
	}

	@Before("cut()")
	public void checkAccessToken(JoinPoint joinPoint) {
		log.debug("JWTLoginAspect.checkAccessToken");

		Signature signature = joinPoint.getSignature();
		log.debug("JWTLoginAspect.checkAccessToken for method: {}", signature.getName());

		HttpServletRequest request = CommonAOP.extractHttpServletRequest(joinPoint.getArgs());

		if (request == null) {
			log.warn("HttpServletRequest is missing from method arguments.");
			throw new UnauthorizedAccessAttemptException("Access token is missing.");
		}

		Cookie cookie = CommonAOP.extractAccessToken(request)
			.orElseThrow(() -> new UnauthorizedAccessAttemptException("Access token is missing."));
		log.debug("Access token: {}", cookie.getValue());

		TokenValidationResult tokenValidationResult = jwtUtil.validateToken(cookie.getValue());

		if (!tokenValidationResult.isValid()) {
			if (tokenValidationResult.isExpired()) {
				throw new UnauthorizedAccessAttemptException(tokenValidationResult.getMessage());
			}

			throw new UnauthorizedAccessAttemptException(tokenValidationResult.getMessage());
		}
	}
}
