package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.RandomCodeGenerator;
import com.bjcareer.userservice.domain.Redis;
import com.bjcareer.userservice.domain.Telegram;
import com.bjcareer.userservice.domain.User;
import com.bjcareer.userservice.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.exceptions.TelegramCommunicationException;
import com.bjcareer.userservice.exceptions.UserAlreadyExistsException;
import com.bjcareer.userservice.repository.DatabaseRepository;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.vo.TokenVO;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
public class RegisterService {
    private final DatabaseRepository databaseRepository;
    private final RedisRepository redisRepository;
    private final Telegram telegramDomain;
    private final Redis redisDomain;

    public Long generateRandomTokenForAuthentication(String telegramId) {
        Long EXPIRATION_TIME = 60L;
        Long generate = RandomCodeGenerator.generate();
        boolean isSend = telegramDomain.sendCode(telegramId, generate);

        if (!isSend) {
            throw new TelegramCommunicationException("통신 에러");
        }

        redisRepository.saveToken(new TokenVO(telegramId, generate), EXPIRATION_TIME);
        return generate;
    }

    public boolean verifyToken(String telegramId, Long token) {
        Optional<TokenVO> tokebByTelegramId = redisRepository.findTokenByTelegramId(telegramId);

        if (tokebByTelegramId.isEmpty()){
            return false;
        }

        TokenVO tokenVO = tokebByTelegramId.get();
        return token.equals(tokenVO.getToken());
    }

    @Transactional
    public String registerService(User user) {
        String LOCK_KEY = "auth:register:lock";
        boolean isLock = redisDomain.tryLock(LOCK_KEY);

        if (!isLock){
            redisDomain.releaselock(LOCK_KEY);
            throw new RedisLockAcquisitionException("Server is busy");
        }

        Optional<User> userIdInDatabase = databaseRepository.findByUserId(user.getUserId());
        userIdInDatabase.ifPresent(u -> {
            redisDomain.releaselock(LOCK_KEY);
            throw new UserAlreadyExistsException("ID already exists: " + user.getUserId());
        });

        databaseRepository.save(user);

        redisDomain.releaselock(LOCK_KEY);
        return user.getId();
    }
}
