package com.bjcareer.userservice.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.aop.HasRole;
import com.bjcareer.userservice.application.register.RegisterUsecase;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.in.api.request.MobileAuthenticationVerifyRequestDTO;
import com.bjcareer.userservice.in.api.request.RegisterRequestDTO;
import com.bjcareer.userservice.in.api.response.MobileAuthenticationResponseDTO;
import com.bjcareer.userservice.in.api.response.MobileAuthenticationVerifyResponseDTO;
import com.bjcareer.userservice.in.api.response.RegisterResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v0/auth")
public class RegisterController {
    private final RegisterUsecase registerUsecase;

    @PostMapping("/register")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        User requestUser = new User(request.getUserId(), request.getPassword(), request.getTelegramId());
        Long id = registerUsecase.registerService(requestUser);
        return new ResponseEntity<>(new RegisterResponseDTO(id.toString()), HttpStatus.OK);
    }

    @PostMapping("/telegram-authentication")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> generateTokenForRegister(@RequestBody RegisterRequestDTO request) {
        Long token = registerUsecase.generateRandomTokenForAuthentication(request.getTelegramId());
        return new ResponseEntity<>(new MobileAuthenticationResponseDTO(token, request.getTelegramId()), HttpStatus.OK);
    }

    @PostMapping("/verify-token")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> verifyTelegramAccount(@RequestBody MobileAuthenticationVerifyRequestDTO request) {
        log.debug("Id를 {} 위해서 셍성된 랜덤 벨류의 값은? {}", request.getTelegramId(), request.getToken());
        boolean result = registerUsecase.verifyToken(request.getTelegramId(), request.getToken());
        return new ResponseEntity<>(new MobileAuthenticationVerifyResponseDTO(result), HttpStatus.OK);
    }
}
