package com.bjcareer.gateway.aop;

import java.util.Arrays;
import java.util.Optional;

import com.bjcareer.gateway.common.CookieHelper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonAOP {
	// HttpServletRequest 추출 메서드
	public static HttpServletRequest extractHttpServletRequest(Object[] args) {
		return Arrays.stream(args)
			.filter(arg -> arg instanceof HttpServletRequest)
			.map(arg -> (HttpServletRequest)arg)
			.findFirst()
			.orElse(null);
	}

	// 쿠키에서 Access Token 추출
	public static Optional<Cookie> extractAccessToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			log.warn("No cookies present in the request.");
			return Optional.empty();
		}

		return Arrays.stream(cookies)
			.filter(cookie -> CookieHelper.ACCESS_TOKEN.equals(cookie.getName()))
			.findFirst();
	}

}
