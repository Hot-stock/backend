package com.bjcareer.search.application.search;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.out.persistence.repository.chart.StockChartRepositoryAdapter;

@SpringBootTest
class NewsServiceTest {
	@Autowired
	NewsService newsService;
	@Autowired
	StockChartRepositoryAdapter stockChartRepositoryAdapter;

	@Test
	@Rollback(false)
	void 상승_이유를_찾아서_저장이_되는지_체크() {
		Optional<GTPNewsDomain> result = newsService.findNewsToChartByDate("우신시스템", LocalDate.of(2020, 9, 2));
		assertTrue(result.isPresent());

		Optional<GTPNewsDomain> gtpNewsDomain = stockChartRepositoryAdapter.findChartByDate("우신시스템",
			LocalDate.of(2020, 9, 2)).loadNewByDate(LocalDate.of(2020, 9, 2));

		assertTrue(gtpNewsDomain.isPresent());
		assertEquals(result.get(), gtpNewsDomain.get());
	}
}
