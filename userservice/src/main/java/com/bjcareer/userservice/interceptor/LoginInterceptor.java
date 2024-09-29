package com.bjcareer.userservice.interceptor;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.servlet.HandlerInterceptor;

import com.bjcareer.userservice.application.auth.token.ports.TokenUsecase;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String SESSION_ID = "sessionId";

    private final TokenUsecase tokenUsecase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Optional<Cookie> accessToken = Arrays.stream(cookies).filter(c -> c.getName().equals(ACCESS_TOKEN)).findFirst();
        Optional<Cookie> sessionId = Arrays.stream(cookies).filter(c -> c.getName().equals(SESSION_ID)).findFirst();

        request.setAttribute(SESSION_ID, sessionId.get().getValue());

        if (accessToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (sessionId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return tokenUsecase.verifyAccessToken(accessToken.get().getValue());
    }
}
