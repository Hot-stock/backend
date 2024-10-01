package com.bjcareer.userservice.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.userservice.application.auth.aop.HasRole;
import com.bjcareer.userservice.application.ports.in.RegisterRequestCommand;
import com.bjcareer.userservice.application.ports.in.RegisterUsecase;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.in.api.request.MobileAuthenticationVerifyRequestDTO;
import com.bjcareer.userservice.in.api.request.RegisterRequestDTO;
import com.bjcareer.userservice.in.api.request.VerifyEmailRequestDTO;
import com.bjcareer.userservice.in.api.response.MobileAuthenticationVerifyResponseDTO;
import com.bjcareer.userservice.in.api.response.RegisterResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v0/register")
public class RegisterController {
    private final RegisterUsecase registerUsecase;

    @PostMapping("/verify-email")
    @Operation(summary = "이메일 인증 토큰 생성", description = "사용자가 입력한 이메일로 회원가입 인증을 위한 랜덤 토큰을 생성하고 전송합니다. 중복된 이메일이 있으면 에러를 반환합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "토큰 생성 성공"),
    })
    @HasRole(RoleType.ALL)
    public ResponseEntity generateTokenForRegister(@RequestBody VerifyEmailRequestDTO request) {
        log.debug("이메일 {}에 대한 인증 토큰 생성", request.getEmail());
        registerUsecase.generateRandomTokenForAuthentication(request.getEmail());
        return ResponseEntity.ok().build();  // 상태 코드를 명확하게 명시하여 응답을 전송
    }

    @PostMapping("/verify-token")
    @Operation(summary = "이메일 인증 토큰 검증", description = "사용자가 입력한 이메일로 발송된 토큰을 검증합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "검증 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    })
    @HasRole(RoleType.ALL)
    public ResponseEntity<MobileAuthenticationVerifyResponseDTO> verifyEmailToken(@RequestBody MobileAuthenticationVerifyRequestDTO request) {
        log.debug("이메일 {}에 대해 입력된 토큰 값: {}", request.getEmail(), request.getToken());
        boolean isVerified = registerUsecase.verifyToken(request.getEmail(), request.getToken());

        if (!isVerified) {
            return new ResponseEntity<>(new MobileAuthenticationVerifyResponseDTO(isVerified),
                HttpStatus.BAD_REQUEST);  // 상태 코드를 명확하게 명시하여 에러 응답을 전송
        }

        log.debug("이메일 {}에 대해 입력된 토큰 값 검증 결과: {}", request.getEmail(), isVerified);
        return ResponseEntity.ok(new MobileAuthenticationVerifyResponseDTO(isVerified));
    }

    @PostMapping
    @Operation(summary = "회원가입", description = "사용자가 입력한 이메일 및 패스워드로 회원가입을 진행합니다.", responses = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "중복된 이메일")
    })
    @HasRole(RoleType.ALL)
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        log.debug("회원가입 요청: {}", request.getEmail());
        RegisterRequestCommand command = new RegisterRequestCommand(request.getEmail(), request.getPassword());
        Long id = registerUsecase.registerService(command);
        log.debug("회원가입 완료: {}", id);
        return new ResponseEntity<>(new RegisterResponseDTO(id), HttpStatus.CREATED);
    }

}
