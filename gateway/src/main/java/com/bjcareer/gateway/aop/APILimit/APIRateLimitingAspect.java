package com.bjcareer.gateway.aop.APILimit;

import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.bjcareer.gateway.aop.CommonAOP;
import com.bjcareer.gateway.aop.ports.out.RateLimitPort;
import com.bjcareer.gateway.common.JWTUtil;
import com.bjcareer.gateway.domain.TokenBucket;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class APIRateLimitingAspect {
	public static final String REAL_IP = "X-Forwarded-For";
	private final JWTUtil jwtUtil;
	private final RateLimitPort rateLimitPort;

	@Pointcut("@annotation(com.bjcareer.gateway.aop.APILimit.APIRateLimit)")
	private void cut() {
	}

	@Before("cut()")
	public void rateLimiting(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.debug("Rate Limiting Aspect invoked for method: {}", signature.getName());

		HttpServletRequest request = CommonAOP.extractHttpServletRequest(joinPoint.getArgs());

		if (request == null) {
			log.warn("HttpServletRequest is missing from method arguments.");
			return;
		}

		String clientIp = request.getHeader(REAL_IP);
		log.debug("Client IP: {}", clientIp);

		String key = generateRateLimitKey(request);
		processTokenBucket(key);
	}

	// Rate Limit 키 생성 메서드
	private String generateRateLimitKey(HttpServletRequest request) {
		Optional<Cookie> accessToken = CommonAOP.extractAccessToken(request);

		if (accessToken.isEmpty()) {
			log.debug("No access token found. Using client IP address for rate limit key.");
			return RateLimitPort.BUCKET_KEY + request.getRemoteAddr();
		} else {
			log.debug("Access token found. Using token for rate limit key.");
			Claims claims = jwtUtil.parseToken(accessToken.get().getValue());
			return RateLimitPort.BUCKET_KEY + claims.getSubject();
		}
	}

	// 토큰 버킷 처리 메서드
	private void processTokenBucket(String key) {
		Optional<TokenBucket> tokenBucket = rateLimitPort.loadTokenBucket(key);

		if (tokenBucket.isEmpty()) {
			log.debug("No token bucket found for key: {}. Creating new token bucket.", key);
			rateLimitPort.saveTokenBucket(key, new TokenBucket());
		} else {
			TokenBucket apiTokenBucket = tokenBucket.get();
			log.debug("Existing token bucket found. Available tokens: {}", apiTokenBucket.getAvailableTokens());
			apiTokenBucket.attemptApiCall();
			rateLimitPort.saveTokenBucket(key, apiTokenBucket);
		}
	}
}
