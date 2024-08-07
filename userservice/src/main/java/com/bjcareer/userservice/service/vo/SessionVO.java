package com.bjcareer.userservice.service.vo;

import com.bjcareer.userservice.domain.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SessionVO {
    private final JwtTokenVO jwtTokenVO;
    private final User user;
}
