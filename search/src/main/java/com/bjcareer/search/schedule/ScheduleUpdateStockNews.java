package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.out.api.analyze.AnalyzeServiceServerAdapter;
import com.bjcareer.search.out.api.toss.TossServerAdapter;
import com.bjcareer.search.out.api.toss.dtos.CandleResponseDTO;
import com.bjcareer.search.out.persistence.chart.StockChartRepositoryAdapter;
import com.bjcareer.search.out.persistence.stock.StockRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleUpdateStockNews {
	private final StockRepositoryAdapter stockRepository;
	private final StockChartRepositoryAdapter stockChartRepository;
	private final AnalyzeServiceServerAdapter analyzeServiceServerAdapter;
	private final TossServerAdapter tossServerAdapter;

	// @Scheduled(fixedDelay = 300000)
	// @Transactional
	public void updateNewsOfStock() {
		List<Stock> stocks = stockRepository.findAll().reversed();

		for (Stock stock : stocks) {
			CandleResponseDTO res = tossServerAdapter.getStockPriceURI(stock.getCode(), "day");

			List<CandleResponseDTO.Result.Candle> candles = res.getResult().getCandles();
			double upPoint = getUpPoint(candles, 0);
			String date = candles.get(0).getStartDate().split("T")[0];
			LocalDate target = LocalDate.parse(date).plusDays(1);

			if (upPoint < 6) {
				continue;
			}

			log.info("Stock: {} candles: {}", stock.getName(), target);
			analyzeServiceServerAdapter.updateNewsOfStock(stock.getName(), target.toString());
		}
	}

	private double getUpPoint(List<CandleResponseDTO.Result.Candle> candles, int idx) {
		double upPoint = ((double) (candles.get(idx).getClose() - candles.get(idx).getBase()) / candles.get(idx).getBase()) * 100;
		return upPoint;
	}
}
