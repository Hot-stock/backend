package com.bjcareer.userservice.application.message;

import java.time.LocalDate;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.ports.out.PersistActiveUserPort;
import com.bjcareer.userservice.application.ports.out.message.UserLoggedInRecorderEvent;
import com.bjcareer.userservice.domain.entity.UserActive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLoggedInRecorderEventHandler {
	private final PersistActiveUserPort persistActiveUserPort;

	@EventListener
	@Async
	@Transactional
	public void handle(UserLoggedInRecorderEvent event) {
		LocalDate localDate = LocalDate.now();
		persistActiveUserPort.findUserByLocaleDate(event.getUser(), localDate)
			.orElseGet(() -> {
				UserActive userActive = new UserActive(event.getUser());
				log.info("오늘 최조로 접속한 유저입니다 {}", userActive);
				persistActiveUserPort.persistActiveUser(userActive);
				return userActive;
			});
	}
}
