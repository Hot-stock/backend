package com.bjcareer.userservice.application.message;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.ports.out.SMTPProviderPort;
import com.bjcareer.userservice.application.ports.out.message.AuthCodeSentEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthCodeSentEventHandler {
	private final SMTPProviderPort smtpProviderPort;

	@EventListener
	@Async
	public void handle(AuthCodeSentEvent event) {
		log.debug("handle event: {}", event);
		smtpProviderPort.sendVerificationEmail(event.getEmail(), event.getCode());
	}
}
