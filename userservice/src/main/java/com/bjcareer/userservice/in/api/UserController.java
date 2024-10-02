package com.bjcareer.userservice.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
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

        CookieHelper.setCookieForRefreshToken(response, jwt);
        CookieHelper.setCookieForSessionId(response, jwt);

        LoginResponseDTO res = new LoginResponseDTO(jwt.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshLogin(@CookieValue("sessionId") String sessionId,
        @CookieValue("refreshToken") String refreshToken) {
        log.debug("Refresh token request: {} {}", sessionId, refreshToken);
        TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
        JwtTokenVO jwtTokenVO = tokenUsecase.generateAccessTokenViaRefresh(command);
        LoginResponseDTO res = new LoginResponseDTO(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }
}
