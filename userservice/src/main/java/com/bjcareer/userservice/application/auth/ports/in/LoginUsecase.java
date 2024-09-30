package com.bjcareer.userservice.application.auth.ports.in;

import com.bjcareer.userservice.domain.entity.User;

public interface LoginUsecase {
	User login(LoginCommand command);
}
