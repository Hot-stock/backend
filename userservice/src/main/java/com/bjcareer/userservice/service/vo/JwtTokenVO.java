package com.bjcareer.userservice.service.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenVO {
    private final String accessToken;
    private final String refreshToken;
    private final String sessionId;
    private final Long refreshTokenExpireTime;


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }
}
