package com.bjcareer.userservice.application.auth;

import com.bjcareer.userservice.domain.entity.User;

public interface LoginUsecase {
	void login(User inputUser);
}
