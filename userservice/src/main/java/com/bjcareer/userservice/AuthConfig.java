package com.bjcareer.userservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bjcareer.userservice.application.token.ports.TokenUsecase;
import com.bjcareer.userservice.interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final TokenUsecase tokenUsecase;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(tokenUsecase))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v0/user/login", "/api/v0/user/logout", "/api/v0/auth/register");
    }
}
