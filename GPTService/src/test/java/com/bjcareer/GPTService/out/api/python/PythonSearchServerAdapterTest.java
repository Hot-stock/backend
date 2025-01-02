package com.bjcareer.GPTService.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.schedule.OhlcResponseDTO;
import com.bjcareer.GPTService.schedule.StockChartQueryCommand;

@SpringBootTest
class PythonSearchServerAdapterTest {
	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 지정된_뉴스를_패치할_수_있는지_검증() {
		NewsCommand newsCommand = new NewsCommand("이준석", LocalDate.now(), LocalDate.now());
		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(newsCommand);

		assertNotNull(newsResponseDTOS);
		for (NewsResponseDTO newsResponseDTO : newsResponseDTOS) {
			assertEquals(LocalDate.now(), newsResponseDTO.getDate());
		}

		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(newsResponseDTOS.get(0).getLink(),
			newsCommand.getStartDate());

		assertEquals(LocalDate.now(), originalNews.get().getPubDate());
	}


	@Test
	void 주가연동테스트() {
		NewsCommand newsCommand = new NewsCommand("이준석", LocalDate.of(2024, 12, 17), LocalDate.of(2024, 12, 17));
		List<OhlcResponseDTO> ohlcResponseDTOS = pythonSearchServerAdapter.loadStockChart(
			new StockChartQueryCommand("005930", LocalDate.now().minusDays(1), LocalDate.now()));
		assertNotNull(ohlcResponseDTOS);
		System.out.println("originalNews = " + ohlcResponseDTOS);
	}
}
