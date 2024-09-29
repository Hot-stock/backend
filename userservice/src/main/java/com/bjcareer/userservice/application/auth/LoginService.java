package com.bjcareer.userservice.application.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.repository.DatabaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUsecase {
	private final DatabaseRepository databaseRepository;

	@Transactional(readOnly = true)
	public void login(User inputUser) {
		Optional<User> userFromDatabase = databaseRepository.findByUserId(inputUser.getAlais());

		if (userFromDatabase.isEmpty()) {
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다.");
		}

		User storedUser = userFromDatabase.get();
		boolean isVerify = storedUser.verifyPassword(inputUser.getPassword());

		if (!isVerify) {
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다2.");
		}
	}
}
