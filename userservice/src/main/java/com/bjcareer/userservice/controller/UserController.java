package com.bjcareer.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.ports.AuthWithTokenUsecase;
import com.bjcareer.userservice.application.auth.ports.LoginCommand;
import com.bjcareer.userservice.application.auth.ports.TokenRefreshCommand;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.common.CookieHelper;
import com.bjcareer.userservice.controller.dto.UserDto.LoginRequest;
import com.bjcareer.userservice.controller.dto.UserDto.LoginResponse;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.security.HasRole;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/user")
public class UserController {
    private final AuthWithTokenUsecase usecase;

    @PostMapping("/login")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> Login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginCommand command = new LoginCommand(request.getId(), request.getPassword());
        JwtTokenVO jwt = usecase.login(command);

        CookieHelper.setCookieForRefreshToken(response, jwt);
        CookieHelper.setCookieForSessionId(response, jwt);

        LoginResponse res = new LoginResponse(jwt.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> refreshLogin(@CookieValue("sessionId") String sessionId,
        @CookieValue("refreshToken") String refreshToken) {
        TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
        JwtTokenVO jwtTokenVO = usecase.generateAccessTokenViaRefresh(command);
        LoginResponse res = new LoginResponse(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }
}
