package com.bjcareer.userservice.application.ports.in;

import com.bjcareer.userservice.domain.entity.User;

public interface LoginUsecase {
	User login(LoginCommand command);
}
