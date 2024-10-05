package com.bjcareer.gateway.aop;

import java.time.Clock;
import java.util.Arrays;
import java.util.Optional;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class RateLimitingAspect {
	private final JWTUtil jwtUtil;
	private final RateLimitPort rateLimitPort;
	private final Clock clock;

	@Before("execution(* com.bjcareer.gateway.application..*(..)) && args(request,..)")
	public void rateLimiting(HttpServletRequest request) {
		// 여러 메서드 시그니처에서도 HttpServletRequest만 있으면 실행됨
		log.debug("Rate Limiting: " + request.getRemoteAddr());
		Optional<Cookie> accessToken = Arrays.stream(request.getCookies())
			.filter(c -> c.getName().equals(CookieHelper.ACCESS_TOKEN))
			.findFirst();

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
			rateLimitPort.saveTokenBucket(key, new TokenBucket(clock));
		}
		else{
			log.debug("Token Bucket is Not Empty");
			TokenBucket apiTokenBuket = tokenBucket.get();
			log.debug("Starting API LIMIT : {}", apiTokenBuket.getAvailableTokens());
			apiTokenBuket.attemptApiCall();

			rateLimitPort.saveTokenBucket(key, apiTokenBuket);
		}

		log.debug("Rate Limiting Aspect");
	}
}
