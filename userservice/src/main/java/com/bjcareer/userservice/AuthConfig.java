package com.bjcareer.userservice;

import com.bjcareer.userservice.interceptor.LoginInterceptor;
import com.bjcareer.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtService))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v0/user/login", "/api/v0/user/logout", "/api/v0/auth/register");
    }
}
