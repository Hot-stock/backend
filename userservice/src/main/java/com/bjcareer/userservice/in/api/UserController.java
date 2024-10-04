package com.bjcareer.userservice.in.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
import com.bjcareer.userservice.application.ports.in.LogoutCommand;
import com.bjcareer.userservice.application.ports.in.LogoutUsecase;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.common.CookieHelper;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.in.api.request.LoginRequestDTO;
import com.bjcareer.userservice.in.api.response.LoginResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/auth")
public class UserController {
    private final LoginUsecase loginUsecase;
    private final LogoutUsecase logoutUsecase;
    private final TokenUsecase tokenUsecase;

    @PostMapping("/login")
    @Operation(summary = "로그인 요청", description = "로그인 요청 기능입니다.", responses = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "파라미터 오류"),
        @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    public ResponseEntity<?> Login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginCommand command = new LoginCommand(request.getEmail(), request.getPassword());
        log.debug("Login request: {}", command.getEmail());

        User user = loginUsecase.login(command);
        JwtTokenVO jwt = tokenUsecase.generateJWT(user);

        CookieHelper.setCookieAuthClient(response, jwt);
        LoginResponseDTO res = new LoginResponseDTO(jwt.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "로그아웃 요청 기능입니다.", responses = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "400", description = "로그아웃 실패")
    })
    public ResponseEntity<?> Logout(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
        @CookieValue(CookieHelper.ACCESS_TOKEN) String accessToken, HttpServletResponse response) {
        LogoutCommand command = new LogoutCommand(sessionId, accessToken);
        Optional<JwtTokenVO> logout = logoutUsecase.logout(command);

        if (logout.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CookieHelper.removeCookieForAuthClient(response, logout.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신 요청", description = "토큰 갱신 요청기능입니다.", responses = {
        @ApiResponse(responseCode = "200", description = "갱신 성공"),
        @ApiResponse(responseCode = "401", description = "갱신 실패로 모든 토큰 폐기")
    })
    public ResponseEntity<?> refreshLogin(@CookieValue(CookieHelper.SESSION_ID) String sessionId,
        @CookieValue(CookieHelper.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        log.debug("Refresh token request: {} {}", sessionId, refreshToken);

        TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
        JwtTokenVO jwtTokenVO = tokenUsecase.generateAccessTokenViaRefresh(command);
        CookieHelper.setCookieAuthClient(response, jwtTokenVO);

        LoginResponseDTO res = new LoginResponseDTO(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }
}
