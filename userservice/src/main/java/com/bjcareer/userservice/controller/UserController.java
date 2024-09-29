package com.bjcareer.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.LoginUsecase;
import com.bjcareer.userservice.application.token.TokenUsecase;
import com.bjcareer.userservice.application.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.controller.dto.UserDto.LoginRequest;
import com.bjcareer.userservice.controller.dto.UserDto.LoginResponse;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.security.HasRole;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/user")
public class UserController {
    private final LoginUsecase loginUsecase;
    private final TokenUsecase tokenUsecase;

    @PostMapping("/login")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> Login(@RequestBody LoginRequest request, HttpServletResponse response) {
        User user = new User(request.getId(), request.getPassword(), null);
        loginUsecase.login(user);
        JwtTokenVO jwtTokenVO = tokenUsecase.generateToken(user);

        setCookieForRefreshToken(response, jwtTokenVO);
        setCookieForSessionId(response, jwtTokenVO);

        LoginResponse res = new LoginResponse(jwtTokenVO.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> refreshLogin(@CookieValue("sessionId") String sessionId,
        @CookieValue("refreshToken") String refreshToken) {
        JwtTokenVO jwtTokenVO = tokenUsecase.generateAccessTokenViaRefresh(sessionId, refreshToken);
        LoginResponse res = new LoginResponse(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }

    private void setCookieForRefreshToken(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
        Cookie cookie = new Cookie("refreshToken", jwtTokenVO.getRefreshToken());

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }


    private void setCookieForSessionId(HttpServletResponse response, JwtTokenVO jwtTokenVO) {
        Cookie cookie = new Cookie("sessionId", jwtTokenVO.getSessionId());

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }
}
