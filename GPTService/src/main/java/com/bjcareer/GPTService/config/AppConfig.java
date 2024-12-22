package com.bjcareer.GPTService.config;

import java.time.ZoneId;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {
	public static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

	public static ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		return objectMapper;
	}

	@Bean(name = "customTaskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2); // 기본 쓰레드 개수
		executor.setMaxPoolSize(5); // 최대 쓰레드 개수
		executor.setQueueCapacity(25); // 큐 용량
		executor.setThreadNamePrefix("CustomExecutor-");
		executor.initialize(); // 초기화
		return executor;
	}
}
