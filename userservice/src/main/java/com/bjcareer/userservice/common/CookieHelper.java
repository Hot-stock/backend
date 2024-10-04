package com.bjcareer.userservice.common;

import com.bjcareer.userservice.application.auth.token.JWTUtil;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieHelper {

	public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	public static final String SESSION_ID = "SESSION_ID";

	private static final String COOKIE_PATH = "/";

	// 쿠키 설정 메서드
	public static void setCookieAuthClient(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		addCookie(response, ACCESS_TOKEN, jwtTokenVO.getAccessToken(), JWTUtil.ACCESS_TOKEN_EXPIRE_DURATION_SEC);
		addCookie(response, REFRESH_TOKEN, jwtTokenVO.getRefreshToken(),
			JWTUtil.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
		addCookie(response, SESSION_ID, jwtTokenVO.getSessionId(), JWTUtil.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
	}

	// 쿠키 제거 메서드
	public static void removeCookieForAuthClient(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		removeCookie(response, ACCESS_TOKEN, jwtTokenVO.getAccessToken());
		removeCookie(response, REFRESH_TOKEN, jwtTokenVO.getRefreshToken());
		removeCookie(response, SESSION_ID, jwtTokenVO.getSessionId());
	}

	// 쿠키 추가 헬퍼 메서드
	private static void addCookie(HttpServletResponse response, String name, String value,
		int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(COOKIE_PATH);
		cookie.setHttpOnly(true);
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
