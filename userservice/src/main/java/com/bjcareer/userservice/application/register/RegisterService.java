package com.bjcareer.userservice.application.register;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;
import com.bjcareer.userservice.application.exceptions.VerifyTokenDoesNotExist;
import com.bjcareer.userservice.application.ports.in.RegisterRequestCommand;
import com.bjcareer.userservice.application.ports.in.RegisterUsecase;
import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.application.ports.out.LoadTokenPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.application.ports.out.SaveTokenPort;
import com.bjcareer.userservice.application.ports.out.message.AuthCodeSentEvent;
import com.bjcareer.userservice.domain.RandomCodeGenerator;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.exceptions.UserAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterService implements RegisterUsecase {
	private final SaveTokenPort saveTokenPort;
	private final LoadTokenPort loadToken;
	private final LoadUserPort loadUserPort;
	private final CreateUserPort createUserPort;

	private final ApplicationEventPublisher eventPublisher;

	public Long generateRandomTokenForAuthentication(String emailId) {
		Long generate = RandomCodeGenerator.generate();
		saveTokenPort.saveVerificationToken(new TokenVO(emailId, generate));
		eventPublisher.publishEvent(new AuthCodeSentEvent(emailId, generate));
		return generate;
	}

	public boolean verifyToken(String email, Long token) {
		log.debug("verifyToken email: {}, token: {}", email, token);
		Optional<TokenVO> tokenByemail = loadToken.loadVerificationTokenByEmail(email);

		if (tokenByemail.isEmpty()) {
			log.error("Token not found");
			return false;
		}
		log.debug("tokenByemail: {}", tokenByemail);

		TokenVO tokenVO = tokenByemail.get();
		boolean isSame = token.equals(tokenVO.getToken());

		if (isSame) {
			log.info("Token verified");
			saveTokenPort.saveVerifiedUser(tokenVO);
			return true;
		}

		log.error("Token not verified {} {}", token, tokenVO.getToken());
		return false;
	}

	@Transactional
	public Long registerService(RegisterRequestCommand command) {
		loadToken.loadVerifiedUserByEmail(command.getEmail())
			.orElseThrow(() -> new VerifyTokenDoesNotExist("Token not found"));

		Optional<User> userIdInDatabase = loadUserPort.findByEmail(command.getEmail());

		userIdInDatabase.ifPresent(u -> {
			throw new UserAlreadyExistsException("ID already exists: " + command.getEmail());
		});

		User user = new User(command.getEmail(), command.getPassword());
		createUserPort.save(user);
		return user.getId();
	}
}
