package com.bjcareer.userservice.application.ports.out;

import java.util.Optional;

import com.bjcareer.userservice.domain.entity.User;

public interface LoadUserPort {
	Optional<User> findByEmail(String email);
}
