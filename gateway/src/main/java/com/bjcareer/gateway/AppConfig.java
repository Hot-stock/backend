package com.bjcareer.gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bjcareer.gateway.interceptor.LoggingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {
	private final LoggingInterceptor loggingInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 모든 경로에 인터셉터 적용
		registry.addInterceptor(loggingInterceptor)
			.addPathPatterns("/**"); // 모든 경로에 대해 인터셉터 적용
	}
}
