package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemoryRepositoryTest {

    @Autowired
    private DatabaseRepository memoryRepository;

    public static final String USER_ID = "admin";
    public static final String PASSWORD = "admin";
    public static final String TELEGRAM_ID = "admin";
    User user;


    @BeforeEach
    void setUp() {
        user = new User(USER_ID, PASSWORD, TELEGRAM_ID);
    }

    @Test
    void save() {
        assertDoesNotThrow(() ->memoryRepository.save(user));
    }

    @Test
    void findByUserId() {
        Optional<User> byUserId = memoryRepository.findByUserId(USER_ID);
        assertTrue(byUserId.isEmpty());

        memoryRepository.save(user);
        byUserId = memoryRepository.findByUserId(USER_ID);
        assertTrue(byUserId.isPresent());
    }
}