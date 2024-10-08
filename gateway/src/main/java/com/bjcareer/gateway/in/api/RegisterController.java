package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.out.RegisterCommand;
import com.bjcareer.gateway.application.ports.out.RegisterServerPort;
import com.bjcareer.gateway.application.ports.out.VerifyTokenCommand;
import com.bjcareer.gateway.domain.EmailCommand;
import com.bjcareer.gateway.domain.RegisterDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.request.MobileAuthenticationVerifyRequestDTO;
import com.bjcareer.gateway.in.api.request.RegisterRequestDTO;
import com.bjcareer.gateway.in.api.request.VerifyEmailRequestDTO;
import com.bjcareer.gateway.in.api.response.MobileAuthenticationVerifyResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/register")
public class RegisterController {
	private final RegisterServerPort port;

	@PostMapping
	public ResponseEntity<ResponseDomain<RegisterDomain>> register(@RequestBody RegisterRequestDTO request) {
		log.debug("Register request: {}", request.getEmail());
		System.out.println("request = " + request);

		RegisterCommand command = new RegisterCommand(request.getEmail(), request.getPassword());
		ResponseDomain<RegisterDomain> response = port.register(command);
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@PostMapping("/verify-token")
	public ResponseEntity<ResponseDomain<MobileAuthenticationVerifyResponseDTO>> verifyToken(
		@RequestBody MobileAuthenticationVerifyRequestDTO request) {
		log.debug("Verify token request: {}", request.getEmail());
		ResponseDomain<MobileAuthenticationVerifyResponseDTO> response = port.verifyToken(
			new VerifyTokenCommand(request.getEmail(), request.getToken()));
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@PostMapping("/verify-email")
	public ResponseEntity<ResponseDomain<Boolean>> verifyToken(@RequestBody VerifyEmailRequestDTO request) {
		log.debug("verify-email request: {}", request.getEmail());
		ResponseDomain<Boolean> response = port.verifyEmail(new EmailCommand(request.getEmail()));
		return new ResponseEntity<>(response, response.getStatusCode());
	}
}
