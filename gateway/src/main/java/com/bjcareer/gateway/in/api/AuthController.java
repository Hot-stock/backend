package com.bjcareer.gateway.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.aop.JWT.JWTLogin;
import com.bjcareer.gateway.application.ports.in.LoginCommand;
import com.bjcareer.gateway.application.ports.in.LogoutCommand;
import com.bjcareer.gateway.application.ports.in.TokenRefreshCommand;
import com.bjcareer.gateway.application.ports.out.AuthServerPort;
import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.domain.JWTDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.request.LoginRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/auth")
public class AuthController {
	private final AuthServerPort port;

	@PostMapping("/login")
	@Operation(summary = "로그인 요청", description = "로그인 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "401", description = "로그인 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<ResponseDomain<String>> Login(HttpServletRequest request,
		@RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
		log.info("Login Request: {}", requestDTO.getEmail());
		LoginCommand command = new LoginCommand(requestDTO.getEmail(), requestDTO.getPassword());

		JWTDomain tokenDomain = port.login(command);

		CookieHelper.setCookieAuthClient(response, CookieHelper.ACCESS_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.ACCESS_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.REFRESH_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.SESSION_ID, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);

		return new ResponseEntity<>(new ResponseDomain<>(HttpStatus.OK, "Login SUCCESS", null), HttpStatus.OK);
	}

	@PostMapping("/logout")
	@JWTLogin
	@Operation(summary = "로그아웃 요청", description = "로그아웃 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "400", description = "로그아웃 실패")
	})
	public ResponseEntity<ResponseDomain<Boolean>> Logout(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
		@CookieValue(CookieHelper.ACCESS_TOKEN) String accessToken, HttpServletResponse response) {
		log.info("Logout request: {} {}", sessionId, accessToken);
		LogoutCommand command = new LogoutCommand(sessionId, accessToken);
		boolean res = port.logout(command);

		if (res) {
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.ACCESS_TOKEN);
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.SESSION_ID);
			CookieHelper.removeCookieForAuthClient(response, CookieHelper.REFRESH_TOKEN);

			log.debug("Remove accessToken: {}", CookieHelper.ACCESS_TOKEN);
			log.debug("Remove sessionId: {}", CookieHelper.SESSION_ID);
			log.debug("Remove refreshToken: {}", CookieHelper.REFRESH_TOKEN);
		}

		return new ResponseEntity<>(new ResponseDomain<>(HttpStatus.OK, res, null), HttpStatus.OK);
	}

	@PostMapping("/refresh")
	@JWTLogin
	@Operation(summary = "토큰 갱신 요청", description = "토큰 갱신 요청기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "갱신 성공"),
		@ApiResponse(responseCode = "401", description = "갱신 실패로 모든 토큰 폐기")
	})
	public ResponseEntity<ResponseDomain<Boolean>> refreshLogin(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
		@CookieValue(CookieHelper.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
		log.info("Logout request: {} {}", sessionId, refreshToken);

		TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
		JWTDomain tokenDomain = port.refresh(command);

		CookieHelper.setCookieAuthClient(response, CookieHelper.ACCESS_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.ACCESS_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.REFRESH_TOKEN, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);
		CookieHelper.setCookieAuthClient(response, CookieHelper.SESSION_ID, tokenDomain.getAccessToken(),
			CookieHelper.REFRESH_TOKEN_EXPIRE_DURATION_MILLIS);

		return new ResponseEntity<>(new ResponseDomain<>(HttpStatus.OK, true, null), HttpStatus.OK);
	}
}
