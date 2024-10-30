package com.bjcareer.userservice.application.auth;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.ports.out.message.UserLoggedInRecorderEvent;
import com.bjcareer.userservice.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginUsecase {
	private final LoadUserPort loadUserPort;
	private final ApplicationEventPublisher publisher;

	@Transactional(readOnly = true)
	public User login(LoginCommand command) {
		Optional<User> userFromDatabase = loadUserPort.findByEmail(command.getEmail());
		log.info("User found?: {}", userFromDatabase.isPresent());

		if (userFromDatabase.isEmpty()) {
			log.error("User not found: {}", command.getEmail());
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다.");
		}

		User storedUser = userFromDatabase.get();
		boolean isVerify = storedUser.verifyPassword(command.getPassword());

		log.debug("found user is Verify?: {}", isVerify);

		if (!isVerify) {
			log.error("Password not matched: {}", command.getEmail());
			throw new UnauthorizedAccessAttemptException("잘못된 ID나 PASSWORD를 입력했습니다2.");
		}

		publisher.publishEvent(new UserLoggedInRecorderEvent(storedUser));

		return storedUser;
	}
}
