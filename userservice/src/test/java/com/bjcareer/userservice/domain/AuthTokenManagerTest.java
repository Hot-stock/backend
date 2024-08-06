package com.bjcareer.userservice.domain;

import com.bjcareer.userservice.service.vo.JwtTokenVO;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenManagerTest {
    private final String secretKey = "3ac4218e586878871ab0e9febefe81d83740ece704704af22d6a69db940fefc0";
    private final AuthTokenManager tokenManager = new AuthTokenManager(secretKey);
    private String sessionId;

    @BeforeEach
    void setUp() {
        this.sessionId = UUID.randomUUID().toString();
    }

    @Test
    void generateToken() {
        JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId);
        assertNotNull(jwtTokenVO);
        assertNotNull(jwtTokenVO.getAccessToken());
        assertNotNull(jwtTokenVO.getRefreshToken());
        assertEquals(sessionId, jwtTokenVO.getSessionId());
    }

    @Test
    void verifyTokenSuccess() {
        JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId);

        assertDoesNotThrow(()->tokenManager.verifyToken(jwtTokenVO.getAccessToken()));
        assertDoesNotThrow(()->tokenManager.verifyToken(jwtTokenVO.getRefreshToken()));
    }

    @Test
    void verifyTokenThrowsExceptionForInvalidToken() {
        JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId);

        assertThrows(SignatureException.class, () -> tokenManager.verifyToken(jwtTokenVO.getAccessToken() + "wrongToken"));
        assertThrows(JwtException.class, () -> tokenManager.verifyToken("wrongToken"));
    }

    @Test
    void refreshRequestBeforeExpiration() {
        JwtTokenVO jwtTokenVO = tokenManager.generateToken(sessionId);
        boolean isExpired = tokenManager.validateRefreshTokenExpiration(jwtTokenVO.getAccessToken());
        assertFalse(isExpired);
    }
}
