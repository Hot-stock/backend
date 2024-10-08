package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.domain.EmailCommand;
import com.bjcareer.gateway.domain.RegisterDomain;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.response.MobileAuthenticationVerifyResponseDTO;

public interface RegisterServerPort {
	ResponseDomain<RegisterDomain> register(RegisterCommand command);

	ResponseDomain<Boolean> verifyEmail(EmailCommand command);

	ResponseDomain<MobileAuthenticationVerifyResponseDTO> verifyToken(VerifyTokenCommand command);
}
