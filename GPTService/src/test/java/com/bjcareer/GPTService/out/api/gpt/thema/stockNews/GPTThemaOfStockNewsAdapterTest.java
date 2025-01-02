package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockThemaNewsRepository;

@SpringBootTest
class GPTThemaOfStockNewsAdapterTest {
	@Autowired
	private GPTThemaOfStockNewsAdapter gptThemaOfStockNewsAdapter;

	@Autowired
	private GPTStockThemaNewsRepository gptStockThemaNewsRepository;

	@Autowired
	private GPTStockNewsRepository gptStockNewsRepository;

	@Autowired
	private GPTNewsAdapter gptNewsAdapter;

	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 정확한_형식으로_들어오는지_테스트() {
		String stockName = "지에스이";
		String link = "https://www.yeongnam.com/web/view.php?key=20240813001502331";
		// Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(
		// 	"https://www.yeongnam.com/web/view.php?key=20240813001502331", LocalDate.now());
		// Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(originalNews.get(), stockName,
		// 	originalNews.get().getPubDate());

		Optional<GPTNewsDomain> byLink = gptStockNewsRepository.findByLink(link);
		Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(byLink.get());

		System.out.println("r = " + r);
		assertTrue(r.isPresent());
	}

	@Test
	void 데이터베이스의_뉴스로_검증() {
		String stockName = "지에스이";
		List<GPTNewsDomain> all = gptStockNewsRepository.findAll();

		for (GPTNewsDomain gptNewsDomain : all) {
			if (gptNewsDomain.getStockName().equals(stockName) && gptNewsDomain.isThema()
				&& gptNewsDomain.isRelated()) {
				Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(gptNewsDomain);
				if (r.isPresent()) {
					System.out.println("r = " + r);
					gptStockThemaNewsRepository.save(r.get());
					// return;
				}
			}
		}
	}
}
