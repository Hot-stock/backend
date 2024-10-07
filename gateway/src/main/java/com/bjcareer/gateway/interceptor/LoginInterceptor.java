package com.bjcareer.gateway.interceptor;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.servlet.HandlerInterceptor;

import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.common.JWTUtil;
import com.bjcareer.gateway.common.TokenValidationResult;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
	private final JWTUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		Optional<Cookie> accessToken = Arrays.stream(cookies).filter(c -> c.getName().equals(CookieHelper.ACCESS_TOKEN)).findFirst();
		Optional<Cookie> sessionId = Arrays.stream(cookies).filter(c -> c.getName().equals(CookieHelper.SESSION_ID)).findFirst();

		request.setAttribute(CookieHelper.SESSION_ID, sessionId.get().getValue());

		if (accessToken.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		if (sessionId.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		TokenValidationResult tokenValidationResult = jwtUtil.validateToken(accessToken.get().getValue());

		if (!tokenValidationResult.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

		return true;
	}
}
