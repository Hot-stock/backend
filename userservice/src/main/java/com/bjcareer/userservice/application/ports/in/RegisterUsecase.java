package com.bjcareer.userservice.application.ports.in;

import com.bjcareer.userservice.domain.entity.User;

public interface RegisterUsecase {
	Long generateRandomTokenForAuthentication(String telegramId);
	boolean verifyToken(String telegramId, Long token);
	Long registerService(RegisterRequestCommand command);
}
