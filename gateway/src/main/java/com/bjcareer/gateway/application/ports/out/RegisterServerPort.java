package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.domain.EmailCommand;
import com.bjcareer.gateway.domain.ResponseDomain;

public interface RegisterServerPort {
	ResponseDomain<?> register(RegisterCommand command);

	ResponseDomain<?> verifyEmail(EmailCommand command);

	ResponseDomain<?> verifyToken(VerifyTokenCommand command);
}
