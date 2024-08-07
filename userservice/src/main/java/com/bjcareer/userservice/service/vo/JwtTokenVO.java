package com.bjcareer.userservice.service.vo;

import com.bjcareer.userservice.domain.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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
