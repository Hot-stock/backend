package com.bjcareer.userservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bjcareer.userservice.application.ports.in.TokenUsecase;
import com.bjcareer.userservice.interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final TokenUsecase tokenUsecase;

    private static final String[] EXCLUDE_PATHS = {
        "/api/v0/auth/**",
        "/api/v0/register/**",
        "/swagger-ui/**",
        "/favicon.ico",
        "/api-docs/**",
        "/error"

    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(tokenUsecase))
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns(EXCLUDE_PATHS);
    }
}
