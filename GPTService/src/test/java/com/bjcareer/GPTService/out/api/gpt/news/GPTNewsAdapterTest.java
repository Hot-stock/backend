package com.bjcareer.GPTService.out.api.gpt.news;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTThemaAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;

@SpringBootTest
class GPTNewsAdapterTest {
	@Autowired
	private GPTNewsAdapter gptNewsAdapter;

	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 뉴스_필터링_테스트() {
		String link = "https://www.energy-news.co.kr/news/articleView.html?idxno=207655";
		String stockName = "인텔리안테크";
		OriginalNews originalNews = pythonSearchServerAdapter.fetchNewsBody(link, LocalDate.now()).get();
		Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(originalNews, stockName,
			LocalDate.now());
		assertFalse(stockRaiseReason.get().isRelated());
	}

}
