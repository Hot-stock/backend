package com.bjcareer.GPTService.out.persistence.redis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisThemaRepositoryTest {
	@Autowired RedisThemaRepository redisThemaRepository;

	@Test
	void 테마를_저장할_수_있는지_테스트() {
		redisThemaRepository.updateThema("이재명");
		boolean contains = redisThemaRepository.loadThema().contains("이재명");
		assertTrue(contains);
	}


	@Test
	void 테마_삭제(){
		String s = redisThemaRepository.removeThema("정치적 사태로서의 북미 관계");
	}
}
