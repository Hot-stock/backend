package com.bjcareer.userservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.commonTest.UsecaseTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@UsecaseTest
@SpringBootTest
class UserActiveTest {
	@PersistenceContext
	private EntityManager em;


	@Test
	@Transactional
	void test() {
		User user = new User("test@test.com", "test");
		em.persist(user);

		UserActive userActive = new UserActive(user);
		em.persist(userActive);

		em.flush();
		em.clear();

		System.out.println("userActive = " + userActive.getLastLogin());
	}
}
