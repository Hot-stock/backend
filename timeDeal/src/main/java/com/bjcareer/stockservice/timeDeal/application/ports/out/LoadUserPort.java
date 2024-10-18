package com.bjcareer.stockservice.timeDeal.application.ports.out;

public interface LoadUserPort {
	Long loadUserUsingSessionId(String sessionId);
}
