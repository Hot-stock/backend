package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.NewsServiceUsecase;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRankService {
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final MarketRankingPort marketRankingPort;
	private final NewsServiceUsecase newsServiceUsecase;
	private final StockChartRepositoryPort stockChartRepositoryPort;

	@Scheduled(fixedDelay = 60000)
	@Transactional
	public void run() {
		List<Stock> rankingKosdaq = pythonSearchServerAdapter.loadRanking(Market.KOSDAQ);
		List<Stock> rankingKospi = pythonSearchServerAdapter.loadRanking(Market.KOSPI);

		saveToCache(rankingKosdaq);
		saveToCache(rankingKospi);
	}

	private void saveToCache(List<Stock> rankStocks) {
		for (Stock stock : rankStocks) {
			log.debug("Ranking stock: {}", stock);

			Optional<StockChart> stockChart =
				stockChartRepositoryPort.loadStockChart(stock.getCode());

			if (stockChart.isEmpty()) {
				log.warn("Stock chart is empty: {}", stock);
				StockChart chart = pythonSearchServerAdapter.loadStockChart(new StockChartQueryCommand(stock, true));
				stockChartRepositoryPort.save(chart);
			}

			boolean existInCache = marketRankingPort.isExistInCache(stock);

			if (!existInCache) {
				List<GTPNewsDomain> raiseReasonThatDate = newsServiceUsecase.findRaiseReasonThatDate(stock.getName(),
					LocalDate.now(AppConfig.ZONE_ID));

				raiseReasonThatDate.forEach(news -> marketRankingPort.updateRankingNews(news, stock));
			}
		}
	}
}
