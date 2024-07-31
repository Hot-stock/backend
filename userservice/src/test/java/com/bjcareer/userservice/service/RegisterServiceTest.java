package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.RandomCodeGenerator;
import com.bjcareer.userservice.domain.Redis;
import com.bjcareer.userservice.domain.Telegram;
import com.bjcareer.userservice.domain.User;
import com.bjcareer.userservice.repository.DatabaseRepository;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.service.exceptions.TelegramCommunicationException;
import com.bjcareer.userservice.service.exceptions.UserAlreadyExistException;
import com.bjcareer.userservice.service.vo.TokenVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    public static final int EXPIRATION_TIME = 60;
    Telegram telegram;
    RedisRepository redisRepository;
    DatabaseRepository databaseRepository;
    RegisterService registerService;
    Redis redis;

    @BeforeEach
    void setUp() {
        redis = mock(Redis.class);
        telegram = mock(Telegram.class);
        redisRepository = mock(RedisRepository.class);
        databaseRepository = mock(DatabaseRepository.class);

        registerService = new RegisterService(databaseRepository, redisRepository, telegram, redis);
    }

    @Test
    void 텔레그렘으로_토큰_보내기_성공() {
//        gvien
        given(telegram.sendCode(anyString(), anyLong())).willReturn(true);

        //when & then
        assertDoesNotThrow(() -> registerService.generateRandomTokenForAuthentication("test"));
    }

    @Test
    void 텔레그렘으로_토큰_보내기_실패() {
//        gvien
        given(telegram.sendCode(anyString(), anyLong())).willThrow(TelegramCommunicationException.class);

        //when & then
        assertThrows(TelegramCommunicationException.class, () -> registerService.generateRandomTokenForAuthentication("test"));
    }

    @Test
    void 사용자가_전달한_토큰이_서버에_저장된_정보와_일치함(){
        Long generate = RandomCodeGenerator.generate();
        String telegramID = "test1234";

        given(redisRepository.findTokebByTelegramId(telegramID)).willReturn(Optional.of(new TokenVO(telegramID, generate)));

        //when
        boolean b = registerService.verifyToken(telegramID, generate);

        //then
        assertTrue(b);
    }

    @Test
    void 사용자가_전달한_토큰이_서버에_저장된_정보가_일치하지_않음(){
        Long generate = RandomCodeGenerator.generate();
        String telegramID = "test1234";
        Long wrongToken = 1L;

        given(redisRepository.findTokebByTelegramId(telegramID)).willReturn(Optional.of(new TokenVO(telegramID, wrongToken)));

        //when
        boolean b = registerService.verifyToken(telegramID, generate);

        //then
        assertFalse(b);
    }

    @Test
    void 사용자가_회원가입을_성공함(){
        Long generate = RandomCodeGenerator.generate();
        User user = new User("ID", "PASSWORD", "TelegramID");

        given(redis.tryLock(anyString())).willReturn(true);
        given(databaseRepository.findByUserId(anyString())).willReturn(Optional.empty());
        given(databaseRepository.save(user)).willReturn(true);

        //when
        String id = registerService.registerService(user);

        //then
        assertEquals(user.getId(), id);
    }

    @Test
    void 사용자가_중복된_Id로_회원가입에_실패함(){
        User user = new User("ID", "PASSWORD", "TelegramID");

        given(redis.tryLock(anyString())).willReturn(true);
        given(databaseRepository.findByUserId(anyString())).willReturn(Optional.of(user));


        //when
        assertThrows(UserAlreadyExistException.class, () -> registerService.registerService(user));
    }

    @Test
    void 사용자가_낙관적락에_의해서_회원가입에_실패함(){
        User user = new User("ID", "PASSWORD", "TelegramID");

        given(redis.tryLock(anyString())).willReturn(true);
        given(databaseRepository.save(user)).willReturn(false);

        //when
        assertThrows(UserAlreadyExistException.class, () -> registerService.registerService(user));
    }

    @Test
    void 락_획득_실페(){
        User user = new User("ID", "PASSWORD", "TelegramID");

        given(redis.tryLock(anyString())).willReturn(false);

        //when & then
        assertThrows(RedisLockAcquisitionException.class, () -> registerService.registerService(user));
    }
}