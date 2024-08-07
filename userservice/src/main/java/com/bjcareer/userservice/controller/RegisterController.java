package com.bjcareer.userservice.controller;

import com.bjcareer.userservice.controller.dto.RegisterDto.*;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.security.HasRole;
import com.bjcareer.userservice.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/auth")
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private final RegisterService registerService;

    @PostMapping("/telegram-authentication")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> generateTokenForRegister(@RequestBody RegisterDtoRequest request) {
        Long token = registerService.generateRandomTokenForAuthentication(request.getTelegramId());
        return new ResponseEntity<>(new MobileAuthentication(token, request.getTelegramId()), HttpStatus.OK);
    }

    @PostMapping("/verify-token")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> verifyTelegramAccount(@RequestBody MobileAuthenticationVerifyRequest request) {
        log.debug("Id를 {} 위해서 셍성된 랜덤 벨류의 값은? {}", request.getTelegramId(), request.getToken());
        boolean result = registerService.verifyToken(request.getTelegramId(), request.getToken());
        return new ResponseEntity<>(new MobileAuthenticationVerifyResponse(result), HttpStatus.OK);
    }

    @PostMapping("/register")
    @HasRole(RoleType.ALL)
    public ResponseEntity<?> register(@RequestBody RegisterDtoRequest request) {
        User requestUser = new User(request.getUserId(), request.getPassword(), request.getTelegramId());
        String id = registerService.registerService(requestUser);
        return new ResponseEntity<>(new RegisterResponse(id), HttpStatus.OK);
    }
}
