package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.AuthTokenManager;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
import com.bjcareer.userservice.service.vo.SessionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final RedisRepository redisRepository;
    private final AuthTokenManager authTokenManager;
    private final String OBJECT_KEY = "USER:LOGIN:";

    public JwtTokenVO generateToken(User user) {
        String sessionId = UUID.randomUUID().toString();
        JwtTokenVO jwtTokenVO = authTokenManager.generateToken(sessionId);
        SessionVO sessionVO = new SessionVO(jwtTokenVO, user);

        redisRepository.saveJWT(getObjectKeyForRedis(sessionId), sessionVO, AuthTokenManager.REFRESH_TOKEN_EXPIRE_DURATION_SEC);

        return jwtTokenVO;
    }

    public JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken) {
        SessionVO sessionVO = validateSessionAndToken(sessionId, refreshToken, true);
        JwtTokenVO jwtTokenVO = sessionVO.getJwtTokenVO();

        boolean isVerify = authTokenManager.validateRefreshTokenExpiration(refreshToken);

        if (!isVerify) {
            boolean isRemoved = redisRepository.removeJWT(OBJECT_KEY + jwtTokenVO.getSessionId());

            if (!isRemoved) {
                log.error("Failed to remove JWT from Redis. Please contact the developer.");
            }
            throw new UnauthorizedAccessAttemptException("Token theft is suspected. Please generate a new token.");
        }

        redisRepository.saveJWT(getObjectKeyForRedis(sessionId), sessionVO, jwtTokenVO.getRefreshTokenExpireTime());

        return authTokenManager.generateToken(sessionId);
    }

    public boolean verifyAccessToken(String accessToken) {
        authTokenManager.verifyToken(accessToken);
        return true;
    }

    private SessionVO validateSessionAndToken(String sessionId, String token, boolean isRefreshToken) {
        Optional<SessionVO> authToken = redisRepository.findAuthTokenBySessionId(getObjectKeyForRedis(sessionId));

        if (authToken.isEmpty()) {
            throw new UnauthorizedAccessAttemptException("Invalid session ID.");
        }

        JwtTokenVO jwtTokenVO = authToken.get().getJwtTokenVO();

        if ((isRefreshToken && !jwtTokenVO.getRefreshToken().equals(token)) ||
                (!isRefreshToken && !jwtTokenVO.getAccessToken().equals(token))) {
            throw new UnauthorizedAccessAttemptException("The token does not match.");
        }

        return authToken.get();
    }

    private String getObjectKeyForRedis(String sessionId) {
        return OBJECT_KEY + sessionId;
    }
}
