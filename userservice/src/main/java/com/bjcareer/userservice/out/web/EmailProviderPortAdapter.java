package com.bjcareer.userservice.out.web;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.ports.out.SMTPProviderPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailProviderPortAdapter implements SMTPProviderPort {
	private final JavaMailSender mailSender;

	@Override
	public void sendVerificationEmail(String toEmail, Long verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(toEmail);
		message.setSubject("회원가입 인증번호");
		message.setText("인증번호: " + verificationCode);

		mailSender.send(message);
	}
}
