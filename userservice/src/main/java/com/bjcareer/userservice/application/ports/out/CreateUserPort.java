package com.bjcareer.userservice.application.ports.out;

import com.bjcareer.userservice.domain.entity.User;

public interface CreateUserPort {
	void save(User user);
}
