package com.bjcareer.userservice.controller;


import com.bjcareer.userservice.application.auth.LoginUsecase;
import com.bjcareer.userservice.controller.dto.UserDto.*;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.security.HasRole;
import com.bjcareer.userservice.service.JwtService;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/user")
public class UserController {
    private final LoginUsecase loginUsecase;
    private final JwtService jwtService;

    @PostMapping("/login")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> Login(@RequestBody LoginRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = new User(request.getId(), request.getPassword(), null);
        loginUsecase.login(user);
        JwtTokenVO jwtTokenVO = jwtService.generateToken(user);

        setCookieForRefreshToken(response, jwtTokenVO);
        setCookieForSessionId(response, jwtTokenVO);

        LoginResponse res = new LoginResponse(jwtTokenVO.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> refreshLogin(@CookieValue("sessionId") String sessionId, @CookieValue("refreshToken") String refreshToken) throws AuthenticationException {
        JwtTokenVO jwtTokenVO = jwtService.generateAccessTokenViaRefresh(sessionId, refreshToken);
        LoginResponse res = new LoginResponse(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/test")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> Login(HttpServletRequest request)  {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/admin")
    @HasRole(RoleType.ADMIN)
    public ResponseEntity<?> admin(HttpServletRequest request)  {
        return ResponseEntity.ok(null);
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
