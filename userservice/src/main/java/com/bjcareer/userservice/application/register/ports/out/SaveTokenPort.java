package com.bjcareer.userservice.application.register.ports.out;

import com.bjcareer.userservice.application.auth.token.valueObject.TokenVO;

public interface SaveTokenPort {
	void save(TokenVO token, Long expirationTime);
}
