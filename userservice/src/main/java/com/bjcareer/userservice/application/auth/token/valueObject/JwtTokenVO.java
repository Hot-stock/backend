package com.bjcareer.userservice.application.auth.token.valueObject;

import java.util.List;

import com.bjcareer.userservice.domain.entity.RoleType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtTokenVO {
    private final String accessToken;
    private final String refreshToken;
    private final String sessionId;
    private final Long refreshTokenExpireTime;
    private final List<RoleType> roleType;

}
