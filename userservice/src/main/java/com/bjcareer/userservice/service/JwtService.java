package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.AuthTokenManager;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
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

    public JwtTokenVO generateToken() {
        String sessionId = UUID.randomUUID().toString();
        JwtTokenVO jwtTokenVO = authTokenManager.generateToken(sessionId);
        redisRepository.saveJWT(getObjectKeyForRedis(sessionId), jwtTokenVO, AuthTokenManager.REFRESH_TOKEN_EXPIRE_DURATION_SEC);
        return jwtTokenVO;
    }

    public JwtTokenVO generateAccessTokenViaRefresh(String sessionId, String refreshToken) {
        JwtTokenVO jwtTokenVO = validateSessionAndToken(sessionId, refreshToken, true);

        boolean isVerify = authTokenManager.validateRefreshTokenExpiration(refreshToken);

        if (!isVerify) {
            boolean isRemoved = redisRepository.removeJWT(OBJECT_KEY + jwtTokenVO.getSessionId());

            if (!isRemoved) {
                log.error("Failed to remove JWT from Redis. Please contact the developer.");
            }
            throw new UnauthorizedAccessAttemptException("Token theft is suspected. Please generate a new token.");
        }

        redisRepository.saveJWT(getObjectKeyForRedis(sessionId), jwtTokenVO, jwtTokenVO.getRefreshTokenExpireTime());

        return authTokenManager.generateToken(sessionId);
    }

    public boolean verifyAccessToken(String sessionId, String accessToken) {
        JwtTokenVO jwtTokenVO = validateSessionAndToken(sessionId, accessToken, false);
        authTokenManager.verifyToken(accessToken);
        return true;
    }

    private JwtTokenVO validateSessionAndToken(String sessionId, String token, boolean isRefreshToken) {
        Optional<JwtTokenVO> authToken = redisRepository.findAuthTokenBySessionId(getObjectKeyForRedis(sessionId));

        if (authToken.isEmpty()) {
            throw new UnauthorizedAccessAttemptException("Invalid session ID.");
        }

        JwtTokenVO jwtTokenVO = authToken.get();

        if ((isRefreshToken && !jwtTokenVO.getRefreshToken().equals(token)) ||
                (!isRefreshToken && !jwtTokenVO.getAccessToken().equals(token))) {
            throw new UnauthorizedAccessAttemptException("The token does not match.");
        }

        return jwtTokenVO;
    }

    private String getObjectKeyForRedis(String sessionId) {
        return OBJECT_KEY + sessionId;
    }
}
