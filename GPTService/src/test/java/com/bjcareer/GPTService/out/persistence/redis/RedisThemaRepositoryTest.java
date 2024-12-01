package com.bjcareer.GPTService.out.persistence.redis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisThemaRepositoryTest {
	@Autowired RedisThemaRepository redisThemaRepository;

	@Test
	void 테마를_저장할_수_있는지_테스트(){
		redisThemaRepository.updateThema("테마1");
		String s = redisThemaRepository.loadThema();
	}

	@Test
	void 저장된_테마를_로드할_수_있는지_테스트(){
		redisThemaRepository.updateThema("테마1");
		redisThemaRepository.updateThema("테마2");

		String s = redisThemaRepository.loadThema();

		assertEquals("테마1,테마2", s);
	}


	@Test
	void 테마_삭제(){
		String s = redisThemaRepository.removeThema("국민의힘");
	}
}
