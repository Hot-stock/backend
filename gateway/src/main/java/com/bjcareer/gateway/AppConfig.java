package com.bjcareer.gateway;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
