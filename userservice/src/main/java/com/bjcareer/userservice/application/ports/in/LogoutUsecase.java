package com.bjcareer.userservice.application.ports.in;

import java.util.Optional;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;

public interface LogoutUsecase {
	Optional<JwtTokenVO> logout(LogoutCommand command);
}
