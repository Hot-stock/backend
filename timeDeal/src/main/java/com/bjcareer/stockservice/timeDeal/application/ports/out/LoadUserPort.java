package com.bjcareer.stockservice.timeDeal.application.ports.out;

import com.bjcareer.stockservice.timeDeal.out.api.authServer.UserResponseDTO;

public interface LoadUserPort {
	UserResponseDTO loadUserUsingSessionId(String sessionId);
}
