package com.bjcareer.userservice.out.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.userservice.commonTest.UsecaseTest;

@UsecaseTest
@SpringBootTest
class EmailProviderAdapterTest {
	@Autowired
	EmailProviderPortAdapter emailProviderAdapter;

	@Test
	void testSendVerificationEmail() {
		emailProviderAdapter.sendVerificationEmail("wodhksqw@naver.com", 1234L);
	}
}
