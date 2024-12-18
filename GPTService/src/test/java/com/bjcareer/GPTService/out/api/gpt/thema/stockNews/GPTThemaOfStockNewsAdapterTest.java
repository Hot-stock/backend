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
		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(
			"https://www.pinpointnews.co.kr/news/articleView.html?idxno=306719", LocalDate.now());
		Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(originalNews.get(), "뱅크웨어글로벌",
			originalNews.get().getPubDate());

		System.out.println("stockRaiseReason.get() = " + stockRaiseReason.get());

		Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(stockRaiseReason.get());
		System.out.println("r.get() = " + r.get());
		assertTrue(r.isPresent());
	}
	//https://www.thebigdata.co.kr/view.php?ud=202412090221357106cd1e7f0bdf_23
	// 	https://www.pinpointnews.co.kr/news/articleView.html?idxno=304895
	//https://theviewers.co.kr/View.aspx?No=3468635
}
