package com.bjcareer.gateway.security.vo;

import com.bjcareer.gateway.security.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class JwtTokenVO {
    private final String accessToken;
    private final String refreshToken;
    private final String sessionId;
    private final Long refreshTokenExpireTime;
    private final List<RoleType> roleType;

}
