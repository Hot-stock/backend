package com.bjcareer.userservice.application.register.ports.out;

import java.util.Optional;

import com.bjcareer.userservice.domain.entity.User;

public interface LoadUserPort {
	Optional<User> findByUserAlias(String alais);
}
