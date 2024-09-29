package com.bjcareer.userservice.application.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.auth.ports.LoginCommand;
import com.bjcareer.userservice.application.auth.ports.LoginUsecase;
import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.repository.DatabaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUsecase {
	private final DatabaseRepository databaseRepository;

	@Transactional(readOnly = true)
	public User login(LoginCommand command) {
		Optional<User> userFromDatabase = databaseRepository.findByUserId(command.getId());

		if (userFromDatabase.isEmpty()) {
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다.");
		}

		User storedUser = userFromDatabase.get();
		boolean isVerify = storedUser.verifyPassword(command.getPassword());

		if (!isVerify) {
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다2.");
		}

		return storedUser;
	}
}
