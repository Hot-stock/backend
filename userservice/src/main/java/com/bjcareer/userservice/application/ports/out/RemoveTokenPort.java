package com.bjcareer.userservice.application.ports.out;

public interface RemoveTokenPort {
	boolean removeToken(String sessionId);
}
