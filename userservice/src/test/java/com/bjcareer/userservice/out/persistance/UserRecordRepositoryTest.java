package com.bjcareer.userservice.out.persistance;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.userservice.commonTest.UsecaseTest;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.domain.entity.UserActive;
import com.bjcareer.userservice.out.persistance.repository.ActiveUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@UsecaseTest
@SpringBootTest
@Transactional
class UserRecordRepositoryTest {
	@Autowired
	ActiveUserRepository activeUserRepository;

	@PersistenceContext
	private EntityManager em;

	@Test
	@Transactional(readOnly = false)
	void test() {
		User user = createUser();

		UserActive userActive = new UserActive(user);
		activeUserRepository.save(userActive);


		LocalDate localDate = LocalDate.now();
		UserActive userActive2 = activeUserRepository.findByUserInLocalDate(user, localDate).get();

		assertNotNull(userActive2);
	}

	private User createUser() {
		User user = new User("test@test.com", "password");
		em.persist(user);
		em.flush();
		em.clear();
		return user;
	}
}
