package com.bjcareer.userservice.out.prometheus;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bjcareer.userservice.application.ports.out.PersistActiveUserPort;
import com.bjcareer.userservice.domain.entity.UserActive;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DauServiceAdapter {
	private final MeterRegistry meterRegistry;
	private final PersistActiveUserPort activeUserPort;

	// 하루에 한 번 DAU 값을 갱신하는 스케줄러
	@Scheduled(fixedRate = 3600000)
	public void calculateDAU() {
		List<UserActive> dailyActiveUsers = activeUserPort.findDailyActiveUsers(LocalDate.now());
		meterRegistry.gauge("dau_metric", dailyActiveUsers.size());
	}
}
