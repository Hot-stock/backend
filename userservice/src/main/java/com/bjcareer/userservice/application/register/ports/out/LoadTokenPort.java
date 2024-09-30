package com.bjcareer.userservice.application.register.ports.out;

import java.util.Optional;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;

public interface LoadTokenPort {
	Optional<TokenVO> loadByTelemgramId(String token);
}
