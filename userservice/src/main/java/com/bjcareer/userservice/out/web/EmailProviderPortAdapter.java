package com.bjcareer.userservice.out.web;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.bjcareer.userservice.application.ports.out.SMTPProviderPort;
import com.bjcareer.userservice.domain.EmailMessageDomain;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailProviderPortAdapter implements SMTPProviderPort {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	@Override
	public void sendVerificationEmail(String toEmail, Long verificationCode) {
		MimeMessage message = mailSender.createMimeMessage();

		log.debug("sendVerificationEmail toEmail: {}, verificationCode: {}", toEmail, verificationCode);

		EmailMessageDomain emailMessageDomain = new EmailMessageDomain(message, toEmail, verificationCode,
			templateEngine);
		emailMessageDomain.createMIMEMessage();

		log.debug("sendVerificationEmail message: {}", message);

		mailSender.send(message);

	}
}
