package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.service.vo.JwtTokenVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisRepositoryTest {
    @Autowired private RedisRepository redisRepository;

    public static final String TEST_KEY = "testKey";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final Long EXPIRATION_TIME = 30L;
    private String sessionId = UUID.randomUUID().toString();

    JwtTokenVO jwtTokenVO = new JwtTokenVO(ACCESS_TOKEN, REFRESH_TOKEN, sessionId, EXPIRATION_TIME);

    @AfterEach
    void tearDown() {
        redisRepository.removeJWT(TEST_KEY);
    }

    @Test
    void saveJWT() {
        redisRepository.saveJWT(TEST_KEY, jwtTokenVO, EXPIRATION_TIME);
    }

    @Test
    void removeJWT() {
        JwtTokenVO jwtTokenVO = new JwtTokenVO(ACCESS_TOKEN, REFRESH_TOKEN, sessionId, 123L);
        redisRepository.saveJWT(TEST_KEY, jwtTokenVO, EXPIRATION_TIME);
        redisRepository.removeJWT(TEST_KEY);
        Optional<JwtTokenVO> authToken = redisRepository.findAuthTokenBySessionId(TEST_KEY);
        assertTrue(authToken.isEmpty());
    }


    @Test
    void findAuthToken() {
        JwtTokenVO jwtTokenVO = new JwtTokenVO(ACCESS_TOKEN, REFRESH_TOKEN, sessionId, 123L);
        redisRepository.saveJWT(TEST_KEY, jwtTokenVO, EXPIRATION_TIME);

        Optional<JwtTokenVO> authToken = redisRepository.findAuthTokenBySessionId(TEST_KEY);
        assertTrue(authToken.isPresent());
    }
}