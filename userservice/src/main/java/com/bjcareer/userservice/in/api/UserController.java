package com.bjcareer.userservice.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.aop.HasRole;
import com.bjcareer.userservice.application.ports.in.AuthWithTokenUsecase;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.TokenRefreshCommand;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.common.CookieHelper;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.in.api.request.LoginRequestDTO;
import com.bjcareer.userservice.in.api.response.LoginResponseDTO;

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
    public ResponseEntity<?> Login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginCommand command = new LoginCommand(request.getId(), request.getPassword());
        JwtTokenVO jwt = usecase.login(command);

        CookieHelper.setCookieForRefreshToken(response, jwt);
        CookieHelper.setCookieForSessionId(response, jwt);

        LoginResponseDTO res = new LoginResponseDTO(jwt.getAccessToken());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshLogin(@CookieValue("sessionId") String sessionId,
        @CookieValue("refreshToken") String refreshToken) {
        TokenRefreshCommand command = new TokenRefreshCommand(sessionId, refreshToken);
        JwtTokenVO jwtTokenVO = usecase.generateAccessTokenViaRefresh(command);
        LoginResponseDTO res = new LoginResponseDTO(jwtTokenVO.getAccessToken());
        return ResponseEntity.ok(res);
    }
}
