package com.bjcareer.gateway.aop;

import java.util.Arrays;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.bjcareer.gateway.aop.ports.out.RateLimitPort;
import com.bjcareer.gateway.common.CookieHelper;
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

	@Pointcut("@annotation(com.bjcareer.gateway.aop.APIRateLimit)")
	private void cut() {
	}

	@Before("cut()")
	public void rateLimiting(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.debug("Rate Limiting Aspect {}", signature.getName());

		//매개변수들 읽음
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = null;
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest)arg;
			}
		}

		if (request == null) {
			log.debug("Request is Null");
			return;
		}

		String clientIp = request.getHeader(REAL_IP);
		log.debug("Rate Limiting: " + clientIp);
		Optional<Cookie> accessToken = Optional.empty();

		if (request.getCookies() == null) {
			log.debug("Cookies is Null");
		}else{
			accessToken = Arrays.stream(request.getCookies())
				.filter(c -> c.getName().equals(CookieHelper.ACCESS_TOKEN))
				.findFirst();
		}

		String key = "";
		if (accessToken.isEmpty()) {
			log.debug("Access Token is Empty Using IP Address");
			key = RateLimitPort.BUCKET_KEY + request.getRemoteAddr();
		} else {
			log.debug("Access Token is Not Empty Using Access Token");
			Claims claims = jwtUtil.parseToken(accessToken.get().getValue());
			key = RateLimitPort.BUCKET_KEY + claims.getSubject();
		}

		Optional<TokenBucket> tokenBucket = rateLimitPort.loadTokenBucket(key);

		//레디스에 keyset없으면
		if (tokenBucket.isEmpty()) {
			log.debug("Token Bucket is Empty");
			rateLimitPort.saveTokenBucket(key, new TokenBucket());
		} else {
			log.debug("Token Bucket is Not Empty");
			TokenBucket apiTokenBuket = tokenBucket.get();
			log.debug("Starting API LIMIT : {}", apiTokenBuket.getAvailableTokens());
			apiTokenBuket.attemptApiCall();

			rateLimitPort.saveTokenBucket(key, apiTokenBuket);
		}

		log.debug("Rate Limiting Aspect");
	}
}