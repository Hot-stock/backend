package com.bjcareer.userservice.application.ports.out;

public interface SMTPProviderPort {
	void sendVerificationEmail(String toEmail, Long verificationCode);
}
