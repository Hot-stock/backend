package com.bjcareer.userservice.application.ports.in;

public interface RegisterUsecase {
	Long generateRandomTokenForAuthentication(String email);
	boolean verifyToken(String telegramId, Long token);
	Long registerService(RegisterRequestCommand command);
}
