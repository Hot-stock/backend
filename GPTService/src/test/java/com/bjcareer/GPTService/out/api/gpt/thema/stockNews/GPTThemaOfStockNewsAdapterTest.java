package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;

@SpringBootTest
class GPTThemaOfStockNewsAdapterTest {
	@Autowired
	private GPTThemaOfStockNewsAdapter gptThemaOfStockNewsAdapter;

	@Autowired
	private GPTNewsAdapter gptNewsAdapter;

	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 정확한_형식으로_들어오는지_테스트() {
		String stockName = "태광";
		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(
			"https://view.asiae.co.kr/article/2024121907592412252", LocalDate.now());
		Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(originalNews.get(), stockName,
			originalNews.get().getPubDate());

		Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(stockRaiseReason.get());
		assertTrue(r.isPresent());
	}
}
