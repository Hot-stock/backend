package com.bjcareer.userservice.application.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.userservice.application.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.ports.in.LoginCommand;
import com.bjcareer.userservice.application.ports.in.LoginUsecase;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.ports.out.message.UserLoggedInRecorderEvent;
import com.bjcareer.userservice.domain.entity.User;

class LoginServiceTest {
	LoginUsecase loginUsecase;
	LoadUserPort loadUserPort;
	ApplicationEventPublisher publisher;

	@BeforeEach
	void setUp() {
		loadUserPort = mock(LoadUserPort.class);
		publisher = mock(ApplicationEventPublisher.class);
		loginUsecase = new LoginService(loadUserPort, publisher);
	}

	@Test
	void login_faild_when_invaild_id() {
		LoginCommand command = new LoginCommand("test", "test");
		when(loadUserPort.findByEmail(command.getEmail())).thenReturn(Optional.empty());

		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginUsecase.login(command),
			"잘못된 ID나 PASSWORD를 입력했습니다.");
	}

	@Test
	void login_faild_when_invaild_password() {
		LoginCommand command = new LoginCommand("test", "test1");
		User user = new User("test", "test2");

		when(loadUserPort.findByEmail(command.getEmail())).thenReturn(Optional.of(user));
		assertThrows(UnauthorizedAccessAttemptException.class, () -> loginUsecase.login(command),
			"잘못된 ID나 PASSWORD를 입력했습니다.");
	}

	@Test
	void login_success() {
		LoginCommand command = new LoginCommand("test", "test1");
		User user = new User("test", "test1");

		when(loadUserPort.findByEmail(command.getEmail())).thenReturn(Optional.of(user));

		loginUsecase.login(command);

		verify(publisher).publishEvent(any(UserLoggedInRecorderEvent.class));
	}
}
