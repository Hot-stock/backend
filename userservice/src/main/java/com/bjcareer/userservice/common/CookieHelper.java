package com.bjcareer.userservice.common;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieHelper {

	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String SESSION_ID = "sessionId";

	private static final int MAX_AGE_ONE_WEEK = 60 * 60 * 24 * 7;
	private static final String COOKIE_PATH = "/";

	// 쿠키 설정 메서드
	public static void setCookieAuthClient(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		addCookie(response, REFRESH_TOKEN, jwtTokenVO.getRefreshToken(), true, MAX_AGE_ONE_WEEK);
		addCookie(response, ACCESS_TOKEN, jwtTokenVO.getAccessToken(), true, MAX_AGE_ONE_WEEK);
		addCookie(response, SESSION_ID, jwtTokenVO.getSessionId(), true, MAX_AGE_ONE_WEEK);
	}

	// 쿠키 제거 메서드
	public static void removeCookieForAuthClient(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		removeCookie(response, REFRESH_TOKEN, jwtTokenVO.getRefreshToken());
		removeCookie(response, ACCESS_TOKEN, jwtTokenVO.getAccessToken());
		removeCookie(response, SESSION_ID, jwtTokenVO.getSessionId());
	}

	// 쿠키 추가 헬퍼 메서드
	private static void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly,
		int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(COOKIE_PATH);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	// 쿠키 삭제 헬퍼 메서드
	private static void removeCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(0); // 0으로 설정하여 쿠키 삭제
		response.addCookie(cookie);
	}
}
