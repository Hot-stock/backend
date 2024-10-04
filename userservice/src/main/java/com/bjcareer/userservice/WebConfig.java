package com.bjcareer.userservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
			.allowedOrigins("http://localhost:3000") // localhost:3000에서의 요청을 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(false); // 쿠키 허용 여부 (false로 설정)
	}
}

