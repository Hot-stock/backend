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
		String stockName = "에스앤디";
		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(
			"https://www.thebigdata.co.kr/view.php?ud=202412190558582669cd1e7f0bdf_23", LocalDate.now());
		Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(originalNews.get(), stockName,
			originalNews.get().getPubDate());

		Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(stockRaiseReason.get());
		System.out.println("r = " + r);
		assertTrue(r.isPresent());
	}

	@Test
	void 뉴스_테마() {
		List<GPTNewsDomain> all = gptStockNewsRepository.findAll();

		for (GPTNewsDomain gptNewsDomain : all) {
			if(!gptNewsDomain.isRelated() || !gptNewsDomain.isThema()) {
				continue;
			}

			if(!gptNewsDomain.getKeywords().contains("우크라이나 재건")){
				continue;
			}

			Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(gptNewsDomain);
			if (r.isEmpty()) {
				continue;
			}

			GPTStockThema save = gptStockThemaNewsRepository.save(r.get());
			System.out.println("TT" + " " + save.getThemaInfo());
		}
	}

	@Test
	void 뉴스_필터링_테스트() {
		List<GPTStockThema> all = gptStockThemaNewsRepository.findAll();

		for (GPTStockThema gptStockThema : all) {
			Optional<GPTNewsDomain> byLink = gptStockNewsRepository.findByLink(gptStockThema.getLink());
			Optional<GPTStockThema> r = gptThemaOfStockNewsAdapter.getThema(byLink.get());

			if (r.isEmpty()) {
				gptStockThemaNewsRepository.delete(gptStockThema);
				continue;
			}

			GPTStockThema save = gptStockThemaNewsRepository.save(r.get());
		}

	}
}
