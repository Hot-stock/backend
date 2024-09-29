package com.bjcareer.userservice;

import com.bjcareer.userservice.application.token.TokenUsecase;
import com.bjcareer.userservice.interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
