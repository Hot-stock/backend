package com.bjcareer.search.repository.stock;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.out.repository.stock.ThemaRepository;

@SpringBootTest
@Transactional
class ThemaRepositoryTest {

	@Autowired
	ThemaRepository themaRepository;

	@Test
	void should_FailIfAnyQueryTakesMoreThan1Second() {
		List<Thema> allThemas = themaRepository.findAll();

		allThemas.forEach(thema -> {
			long durationInMillis = measureExecutionTime(() -> {
				themaRepository.findAllByKeywordContaining(thema.getStock().getName());
			});

			assertThat(durationInMillis)
				.as("The query took more than 1 second")
				.isLessThanOrEqualTo(1000);
		});
	}

	// 메서드의 실행 시간을 측정하는 유틸리티 메서드
	private long measureExecutionTime(Runnable task) {
		long startTime = System.nanoTime();
		task.run();
		long endTime = System.nanoTime();
		return (endTime - startTime) / 1_000_000; // 밀리초로 변환
	}
}
