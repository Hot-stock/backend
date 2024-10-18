package com.bjcareer.userservice.application.ports.in.user;

import com.bjcareer.userservice.domain.entity.User;

public interface SessionUsecase {
	User loadUser(String sessionId);
}
