package com.bjcareer.gateway.out.api.regiter;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.RegisterCommand;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.out.api.CommonConfig;

class RegisterServerAPIAdapterTest {
	Logger logger = new Logger();
	// Test case 1
	@Test
	void testRegister() {
		WebClient webClient = CommonConfig.createWebClient("http://3.34.191.223:8080");
		RegisterServerAPIAdapter registerServerAPIAdapter = new RegisterServerAPIAdapter(webClient, logger);
		RegisterCommand command = new RegisterCommand("wodhksqw@naver.com", "friend77asd@");

		ResponseDomain<?> register = registerServerAPIAdapter.register(command);
		System.out.println("register = " + register);
	}
}
