package com.bjcareer.userservice.application.register;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;
import com.bjcareer.userservice.application.register.ports.in.RegisterUsecase;
import com.bjcareer.userservice.application.register.ports.out.CreateUserPort;
import com.bjcareer.userservice.application.register.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.register.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.register.ports.out.SaveTokenPort;
import com.bjcareer.userservice.domain.RandomCodeGenerator;
import com.bjcareer.userservice.domain.Redis;
import com.bjcareer.userservice.domain.Telegram;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.exceptions.RedisLockAcquisitionException;
import com.bjcareer.userservice.out.persistance.repository.exceptions.TelegramCommunicationException;
import com.bjcareer.userservice.out.persistance.repository.exceptions.UserAlreadyExistsException;

import lombok.Data;

@Service
@Data
public class RegisterService implements RegisterUsecase {
	private final SaveTokenPort saveTokenPort;
	private final LoadTokenPort loadToken;
	private final LoadUserPort loadUserPort;
	private final CreateUserPort createUserPort;

	private final Telegram telegramDomain;
	private final Redis redisDomain;

	public Long generateRandomTokenForAuthentication(String telegramId) {
		Long EXPIRATION_TIME = 60L;
		Long generate = RandomCodeGenerator.generate();
		boolean isSend = telegramDomain.sendCode(telegramId, generate);
		if (!isSend) {
			throw new TelegramCommunicationException("통신 에러");
		}

		saveTokenPort.save(new TokenVO(telegramId, generate), EXPIRATION_TIME);
		return generate;
	}

	public boolean verifyToken(String telegramId, Long token) {
		Optional<TokenVO> tokebByTelegramId = loadToken.loadByTelemgramId(telegramId);

		if (tokebByTelegramId.isEmpty()) {
			return false;
		}

		TokenVO tokenVO = tokebByTelegramId.get();
		return token.equals(tokenVO.getToken());
	}

	@Transactional
	public Long registerService(User user) {
		String LOCK_KEY = "auth:register:lock";
		boolean isLock = redisDomain.tryLock(LOCK_KEY);

		if (!isLock) {
			redisDomain.releaselock(LOCK_KEY);
			throw new RedisLockAcquisitionException("Server is busy");
		}

		Optional<User> userIdInDatabase = loadUserPort.findByUserAlias(user.getAlias());

		userIdInDatabase.ifPresent(u -> {
			redisDomain.releaselock(LOCK_KEY);
			throw new UserAlreadyExistsException("ID already exists: " + user.getAlias());
		});

		createUserPort.save(user);

		redisDomain.releaselock(LOCK_KEY);
		return user.getId();
	}
}
