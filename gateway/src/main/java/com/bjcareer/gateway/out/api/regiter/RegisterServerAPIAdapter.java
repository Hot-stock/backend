package com.bjcareer.gateway.out.api.regiter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.RegisterCommand;
import com.bjcareer.gateway.application.ports.out.RegisterServerPort;
import com.bjcareer.gateway.application.ports.out.VerifyTokenCommand;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.EmailCommand;
import com.bjcareer.gateway.domain.ErrorDomain;
import com.bjcareer.gateway.domain.RegisterDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.response.MobileAuthenticationVerifyResponseDTO;

@Component
public class RegisterServerAPIAdapter implements RegisterServerPort {
	private final WebClient webClient;
	private final Logger log;

	public RegisterServerAPIAdapter(@Qualifier("authWebClient") WebClient webClient, Logger log) {
		this.webClient = webClient;
		this.log = log;
	}

	public ResponseDomain<RegisterDomain> register(RegisterCommand command) {
		ClientResponse response = webClient.post()
			.uri(RegisterServerURI.REGISTER)
			.bodyValue(command)
			.exchange()
			.block();

		log.info("Response status of Register Server for register Request: {}", response.statusCode());

		if (response.statusCode().is2xxSuccessful()) {
			Long id = response.bodyToMono(long.class).block();
			return new ResponseDomain<>(response.statusCode(),
				new RegisterDomain(command.getEmail(), command.getPassword(), id), null);
		} else {
			ErrorDomain error = response.bodyToMono(ErrorDomain.class).block();
			return new ResponseDomain<>(response.statusCode(), null, error);
		}
	}

	@Override
	public ResponseDomain<Boolean> verifyEmail(EmailCommand command) {
		ClientResponse response = webClient.post()
			.uri(RegisterServerURI.VERIFY_EMAIL)
			.bodyValue(command)
			.exchange()
			.block();
		log.info("Response status of Register Server for verifyEmail Request: {}", response.statusCode());
		if (response.statusCode().is2xxSuccessful()) {
			return new ResponseDomain<>(response.statusCode(), true, null);
		} else {
			ErrorDomain error = response.bodyToMono(ErrorDomain.class).block();
			return new ResponseDomain<>(response.statusCode(), null, error);
		}
	}

	@Override
	public ResponseDomain<MobileAuthenticationVerifyResponseDTO> verifyToken(VerifyTokenCommand command) {
		ClientResponse response = webClient.post()
			.uri(RegisterServerURI.VERIFY_TOKEN)
			.bodyValue(command)
			.exchange()
			.block();

		log.info("Response status of Register Server for verifyToken Request: {}", response.statusCode());
		if (response.statusCode().is2xxSuccessful()) {
			MobileAuthenticationVerifyResponseDTO block = response.bodyToMono(
				MobileAuthenticationVerifyResponseDTO.class).block();
			return new ResponseDomain<>(response.statusCode(), block, null);
		} else {
			ErrorDomain error = response.bodyToMono(ErrorDomain.class).block();
			return new ResponseDomain<>(response.statusCode(), null, error);
		}
	}
}
