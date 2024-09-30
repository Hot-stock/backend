package com.bjcareer.userservice.application.register.ports.out;

import com.bjcareer.userservice.domain.entity.User;

public interface CreateUserPort {
	void save(User user);
}
