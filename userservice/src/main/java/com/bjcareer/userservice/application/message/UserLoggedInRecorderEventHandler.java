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
		// 유저가 해당 날짜에 로그인 기록이 없을 때만 새로운 기록 추가
		UserActive userActive1 = persistActiveUserPort.findUserByLocaleDate(event.getUser(), localDate)
			.orElseGet(() -> {
				UserActive userActive = new UserActive(event.getUser());
				persistActiveUserPort.persistActiveUser(userActive);
				return userActive;
			});

		// 유저가 해당 날짜에 로그인 기록이 있을 때는 기존 기록 업데이트
		log.debug("userActive1 = " + userActive1);
	}
}
