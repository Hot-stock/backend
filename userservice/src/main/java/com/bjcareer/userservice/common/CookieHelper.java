package com.bjcareer.userservice.common;

import com.bjcareer.userservice.application.token.valueObject.JwtTokenVO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieHelper {

	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String SESSION_ID = "sessionId";

	public static void setCookieForRefreshToken(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		Cookie cookie = new Cookie(REFRESH_TOKEN, jwtTokenVO.getRefreshToken());

		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		response.addCookie(cookie);
	}

	public static void setCookieForSessionId(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
		Cookie cookie = new Cookie(
			SESSION_ID, jwtTokenVO.getSessionId());

		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		response.addCookie(cookie);
	}
}
