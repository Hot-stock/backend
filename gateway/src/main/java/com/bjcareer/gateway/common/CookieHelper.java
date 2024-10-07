package com.bjcareer.gateway.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieHelper {

	public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	public static final String SESSION_ID = "SESSION_ID";

	private static final String COOKIE_PATH = "/";
	public static final int ACCESS_TOKEN_EXPIRE_DURATION_MILLIS = 30 * 60 * 1000; // 30분
	public static final int REFRESH_TOKEN_EXPIRE_DURATION_MILLIS = 15 * 24 * 60 * 60 * 1000; // 15일

	// 쿠키 설정 메서드
	public static void setCookieAuthClient(HttpServletResponse response, String name, String value, int maxAge) {
		addCookie(response, name, value, true, maxAge);
	}

	// 쿠키 제거 메서드
	public static void removeCookieForAuthClient(HttpServletResponse response, String name) {
		removeCookie(response, name);
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
	private static void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(0); // 0으로 설정하여 쿠키 삭제
		response.addCookie(cookie);
	}
}
