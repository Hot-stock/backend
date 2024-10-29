package com.bjcareer.userservice.out.web;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.bjcareer.userservice.application.ports.out.SMTPProviderPort;
import com.bjcareer.userservice.domain.EmailMessageDomain;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailProviderPortAdapter implements SMTPProviderPort {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	@Override
	public void sendVerificationEmail(String toEmail, Long verificationCode) {
		MimeMessage message = mailSender.createMimeMessage();

		EmailMessageDomain emailMessageDomain = new EmailMessageDomain(message, toEmail, verificationCode,
			templateEngine);
		emailMessageDomain.createMIMEMessage();

		mailSender.send(message);

	}
}
