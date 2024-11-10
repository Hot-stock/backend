package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.GPTInsight;
import com.bjcareer.search.domain.GPTThema;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;

@SpringBootTest
class ChatGPTInsightAdapterTest {
	@Autowired
	private ChatGPTInsightAdapter gptInsightAdapter;

	@Autowired
	private StockChartRepositoryPort stockChartRepositoryPort;

	@Autowired
	private StockRepositoryPort stockRepositoryPort;

	@Autowired
	private ThemaInfoRepositoryPort themaInfoRepositoryPort;

	@Test
	@Rollback(false)
	void testParser() {
		LoadChartAboveThresholdCommand command = new LoadChartAboveThresholdCommand("036890", 7);
		StockChart stockChart = stockChartRepositoryPort.findOhlcAboveThreshold(command);
		ThemaInfo themaInfo = themaInfoRepositoryPort.findByName("우크라이나 재건").get();

		List<GPTThema> news = themaInfo.getNews();

		GPTInsight insight = gptInsightAdapter.getInsight(stockChart.getAllNews(), news, LocalDate.now(AppConfig.ZONE_ID));
		System.out.println("insight = " + insight);

	}

}
