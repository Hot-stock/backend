package com.bjcareer.GPTService.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.out.api.python.Market;
import com.bjcareer.GPTService.out.api.python.MarketResponseDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleUpdateRaiseReason {
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final GPTStockAnalyzeService gptStockAnalyzeService;

	@Scheduled(cron = "0 20 9 * * *", zone = "Asia/Seoul")
	public void updateRankingKeyword() {
		log.info("Start update ranking keyword");

		List<MarketResponseDTO> markets = pythonSearchServerAdapter.loadStockInfo(Market.KOSDAQ);
		markets.addAll(pythonSearchServerAdapter.loadStockInfo(Market.KOSPI));

		for (MarketResponseDTO market : markets) {
			List<OhlcResponseDTO> ohlcResponseDTOS = pythonSearchServerAdapter.loadStockChart(
				new StockChartQueryCommand(market.getSymbol(), LocalDate.now().minusDays(1), LocalDate.now()));

			if (ohlcResponseDTOS.isEmpty()) {
				log.warn("Failed to fetch stock chart for stock: {}", market.getName());
				continue;
			}
			OhlcResponseDTO ohlc = ohlcResponseDTOS.get(0);

			if(7 <= ohlc.getPercentageIncrease()) {
				log.info("Start update ranking keyword for stock: {} Percentage: {} {}", market.getName(), ohlc.getPercentageIncrease(), ohlc.getDate());
				gptStockAnalyzeService.analyzeStockNewsByDateWithStockName(ohlc.getDate(), market.getName());
				log.info("End update ranking keyword for stock: {} Percentage: {}", market.getName(), ohlc.getPercentageIncrease());
			}
		}
	}
}
