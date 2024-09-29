package com.bjcareer.userservice.application.register;

import com.bjcareer.userservice.domain.entity.User;

public interface RegisterUsecase {
	Long generateRandomTokenForAuthentication(String telegramId);
	boolean verifyToken(String telegramId, Long token);
	String registerService(User user);
}
