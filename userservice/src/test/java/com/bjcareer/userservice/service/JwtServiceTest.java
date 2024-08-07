package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.AuthTokenManager;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    public static final String ACCESS_TOKEN = "test1234";
    public static final String REFRESH_TOKEN = "test1234";
    private RedisRepository redisRepository;
    private AuthTokenManager authTokenManager;
    private JwtService jwtService;
    private JwtTokenVO givenTokenVO;
    private String sessionId;
    private User user;

    @BeforeEach
    void setUp() {
        redisRepository = mock(RedisRepository.class);

        user = mock(User.class);
        authTokenManager = mock(AuthTokenManager.class);
        sessionId = UUID.randomUUID().toString();
        givenTokenVO = new JwtTokenVO(ACCESS_TOKEN, REFRESH_TOKEN, sessionId, 3000L);

        jwtService = new JwtService(redisRepository, authTokenManager);
    }

    @Test
    void generateTokenServiceTest() {
        given(authTokenManager.generateToken(anyString())).willReturn(givenTokenVO);

        // when
        JwtTokenVO resultTokenVO = jwtService.generateToken(user);

        // then
        assertEquals(givenTokenVO.getAccessToken(), resultTokenVO.getAccessToken());
        assertEquals(givenTokenVO.getRefreshToken(), resultTokenVO.getRefreshToken());
    }

    @Test
    void generateAccessTokenViaRefresh_TokenNotFoundInBucket() {
        given(authTokenManager.generateToken(sessionId)).willReturn(givenTokenVO);
        given(redisRepository.findAuthTokenBySessionId(anyString())).willReturn(Optional.empty());

        // when & then
        assertThrows(UnauthorizedAccessAttemptException.class,
                () -> jwtService.generateAccessTokenViaRefresh(sessionId, givenTokenVO.getRefreshToken()));
    }

    @Test
    void generateAccessTokenViaRefresh_ValidTokenAlreadyExists() {
        given(redisRepository.findAuthTokenBySessionId(anyString())).willReturn(Optional.of(givenTokenVO));
        given(authTokenManager.validateRefreshTokenExpiration(anyString())).willReturn(false);

        // when & then
        assertThrows(UnauthorizedAccessAttemptException.class,
                () -> jwtService.generateAccessTokenViaRefresh(sessionId, givenTokenVO.getRefreshToken()));
    }

    @Test
    void generateAccessTokenViaRefresh_PreviousTokenExpired_NewTokenGenerated() {
        given(redisRepository.findAuthTokenBySessionId(anyString())).willReturn(Optional.of(givenTokenVO));
        given(authTokenManager.validateRefreshTokenExpiration(anyString())).willReturn(true);

        // when & then
        assertDoesNotThrow(() -> jwtService.generateAccessTokenViaRefresh(sessionId, givenTokenVO.getRefreshToken()));
    }

    @Test
    void generateAccessTokenViaRefresh_ExpiredRefreshToken() {
        given(redisRepository.findAuthTokenBySessionId(anyString())).willReturn(Optional.of(givenTokenVO));
        given(authTokenManager.validateRefreshTokenExpiration(anyString())).willReturn(false);

        // when & then
        assertThrows(UnauthorizedAccessAttemptException.class,
                () -> jwtService.generateAccessTokenViaRefresh(sessionId, givenTokenVO.getRefreshToken()));
    }
}
