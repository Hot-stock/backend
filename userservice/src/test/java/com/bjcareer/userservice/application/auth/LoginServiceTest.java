package com.bjcareer.userservice.application.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.userservice.application.auth.ports.LoginCommand;
import com.bjcareer.userservice.application.auth.ports.LoginUsecase;
import com.bjcareer.userservice.application.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.repository.DatabaseRepository;

class LoginServiceTest {
	LoginUsecase loginUsecase;
	DatabaseRepository databaseRepository;

	@BeforeEach
	void setUp() {
		databaseRepository = mock(DatabaseRepository.class);
		loginUsecase = new LoginService(databaseRepository);
	}

	@Test
	void login_faild_when_invaild_id() {
		LoginCommand command = new LoginCommand("test", "test");
		when(databaseRepository.findByUserId(command.getId())).thenReturn(Optional.empty());

		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginUsecase.login(command),
			"잘못된 ID나 PASSWORD를 입력했습니다.");
	}

	@Test
	void login_faild_when_invaild_password() {
		LoginCommand command = new LoginCommand("test", "test1");
		User user = new User("test", "test2", null);

		when(databaseRepository.findByUserId(command.getId())).thenReturn(Optional.of(user));
		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginUsecase.login(command),
			"잘못된 ID나 PASSWORD를 입력했습니다.");
	}

}
