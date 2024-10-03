package com.bjcareer.userservice.out.persistance;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
	void 일간사용자저장() {
		User user = createUser();

		UserActive userActive = new UserActive(user);
		activeUserRepository.save(userActive);


		LocalDate localDate = LocalDate.now();
		UserActive userActive2 = activeUserRepository.findByUserInLocalDate(user, localDate).get();

		assertNotNull(userActive2);
	}

	@Test
	@Transactional()
	void 저장된_일간_사용자가_없을떄() {
		List<UserActive> dailyActiveUsers = activeUserRepository.findDailyActiveUsers(LocalDate.now());
		assertEquals(0, dailyActiveUsers.size());
	}

	private User createUser() {
		User user = new User("test@test.com", "password");
		em.persist(user);
		em.flush();
		em.clear();
		return user;
	}
}
