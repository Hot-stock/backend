package com.bjcareer.userservice.out.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailProviderPortAdapterTest {

	@Autowired
	private EmailProviderPortAdapter emailProviderPortAdapter;

	@Test
	void testSendVerificationEmail() {
		assertDoesNotThrow(() -> emailProviderPortAdapter.sendVerificationEmail("wodhksqw@naver.com", 1235L));
	}
}
