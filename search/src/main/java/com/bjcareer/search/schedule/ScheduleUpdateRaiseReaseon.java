package com.bjcareer.search.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.information.NewsService;
import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.bjcareer.search.out.api.gpt.thema.ChatGPTThemaAdapter;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleUpdateRaiseReaseon {

	private final StockChartRepositoryPort stockChartRepositoryPort;
	private final ThemaInfoRepositoryPort themaInfoRepositoryPort;
	private final ThemaRepositoryPort themaRepositoryPort;
	private final StockRepositoryPort stockRepositoryPort;
	private final NewsService newsService;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final ChatGPTThemaAdapter chatGPTThemaAdapter;

	// @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	@Transactional
	public void run() {
		Stock stock = stockRepositoryPort.findByName("티플랙스").get();

		LoadChartAboveThresholdCommand loadChartAboveThresholdCommand = new LoadChartAboveThresholdCommand(
			stock.getCode(), 10);

		StockChart chart = stockChartRepositoryPort.findOhlcAboveThreshold(loadChartAboveThresholdCommand);

		stock.getThemas().forEach(thema -> {
			log.info("Thema found: {}", thema.getThemaInfo().getName());
		});

		for (OHLC ohlc : chart.getOhlcList()) {
			List<GPTNewsDomain> raiseReasonThatDate = newsService.findRaiseReasonThatDate(stock.getName(),
				ohlc.getDate());

			if (raiseReasonThatDate.isEmpty()) {
				log.info("No raise reason found for {} {}", stock.getName(), ohlc.getDate());
			} else {
				for (GPTNewsDomain gptNewsDomain : raiseReasonThatDate) {
					log.info("Raise reason found for {} {}", stock.getName(), ohlc.getDate());

					gptNewsDomain.getThemas().forEach(thema -> {
						Optional<ThemaInfo> themaInfoOptional = themaInfoRepositoryPort.findByName(thema.getName());

						themaInfoOptional.ifPresentOrElse(themaInfo -> {
							log.debug("ThemaInfo found: {}", themaInfo.getName());
							if (themaInfo.getName().isEmpty()) {
								return;
							}
							themaRepositoryPort.findByName(themaInfo.getName(), stock.getName()).orElseGet(() -> {
								themaRepositoryPort.save(new Thema(stock, themaInfo));
								return null;
							});
						}, () -> {
							ThemaInfo save = themaInfoRepositoryPort.save(new ThemaInfo(thema.getName()));
							themaRepositoryPort.save(new Thema(stock, save));
						});
					});
				}
			}
		}
	}

	private void getThemaNews(OHLC ohlc, Stock stock) {
		if (ohlc.getNews().isEmpty()) {
			log.info("No news found for {} {}", stock.getName(), ohlc.getDate());

			for (Thema thema : stock.getThemas()) {
				List<News> news = pythonSearchServerAdapter.fetchNews(
					new NewsCommand(thema.getThemaInfo().getName(), ohlc.getDate(), ohlc.getDate()));

				for (News n : news) {
					Optional<GPTThema> gptThema = chatGPTThemaAdapter.summaryThemaNews(n,
						thema.getThemaInfo().getName());

					log.info("News found for {} {}", stock.getName(), gptThema);
				}
			}
		}
	}
}


