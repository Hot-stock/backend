package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.aop.JWT.JWTLogin;
import com.bjcareer.gateway.application.ports.in.AuthUsecase;
import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.domain.JWTDomain;
import com.bjcareer.gateway.in.api.request.LoginRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/auth")
public class AuthController {
	private final AuthUsecase authUsecase;

	@PostMapping("/login")
	@Operation(summary = "로그인 요청", description = "로그인 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "401", description = "로그인 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<?> Login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
		log.debug("Login request: {}", request.getEmail());
		LoginCommand command = new LoginCommand(request.getEmail(), request.getPassword());
		JWTDomain tokenDomain = authUsecase.loginUsecase(command);

		CookieHelper.setCookieAuthClient(response, CookieHelper.ACCESS_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.ACCESS_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.REFRESH_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.SESSION_ID, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	@JWTLogin
	@Operation(summary = "로그아웃 요청", description = "로그아웃 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "400", description = "로그아웃 실패")
	})
	public ResponseEntity<?> Logout(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
		@CookieValue(CookieHelper.ACCESS_TOKEN) String accessToken, HttpServletResponse response) {
		log.debug("Logout request: {}", sessionId);
		LogoutCommand command = new LogoutCommand(sessionId, accessToken);
		boolean res = authUsecase.logoutUsercase(command);

		if (res) {
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.ACCESS_TOKEN);
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.SESSION_ID);
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.REFRESH_TOKEN);

			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/refresh")
	@JWTLogin
	@Operation(summary = "토큰 갱신 요청", description = "토큰 갱신 요청기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "갱신 성공"),
		@ApiResponse(responseCode = "401", description = "갱신 실패로 모든 토큰 폐기")
	})
	public ResponseEntity<?> refreshLogin(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
		@CookieValue(CookieHelper.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
		log.debug("Refresh token request: {} {}", sessionId, refreshToken);

		TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
		JWTDomain tokenDomain = authUsecase.refreshUsecase(command);

		CookieHelper.setCookieAuthClient(response, CookieHelper.ACCESS_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.ACCESS_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.REFRESH_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.SESSION_ID, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);

		return ResponseEntity.ok().build();
	}
}
