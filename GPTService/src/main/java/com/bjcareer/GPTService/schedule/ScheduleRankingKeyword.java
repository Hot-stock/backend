package com.bjcareer.GPTService.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.out.api.python.AbsoluteRankKeywordDTO;
import com.bjcareer.GPTService.out.api.python.Market;
import com.bjcareer.GPTService.out.api.python.MarketResponseDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.redis.RedisTrendKeywordRankAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRankingKeyword {
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final RedisTrendKeywordRankAdapter trendKeywordRankAdapter;

	@Scheduled(cron = "0 10 9 * * *", zone = "Asia/Seoul")
	public void updateRankingKeyword() {
		log.info("Start update ranking keyword");

		List<MarketResponseDTO> stocks = pythonSearchServerAdapter.loadStockInfo(Market.KOSDAQ);
		stocks.addAll(pythonSearchServerAdapter.loadStockInfo(Market.KOSPI));

		for (MarketResponseDTO stock : stocks) {
			List<AbsoluteRankKeywordDTO> absoluteValueOfKeyword = pythonSearchServerAdapter.loadRankKeyword(
				stock.getName());

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.warn("Failed to sleep thread {} {}", e, stock.getName());
			}

			double percentage = getWeightedPercentageWithMovingAverage(absoluteValueOfKeyword, 3);
			log.debug("Start update ranking keyword for stock: {} Percentage: {}", stock.getName(), percentage);
			trendKeywordRankAdapter.updateRanking(stock.getName(), percentage + 0.0);
		}

		log.info("End update ranking keyword");
	}

	private double getWeightedPercentageWithMovingAverage(List<AbsoluteRankKeywordDTO> absoluteValueOfKeyword,
		int movingAverageDays) {
		// 데이터가 충분하지 않으면 0 반환
		if (absoluteValueOfKeyword.size() < movingAverageDays + 1) {
			return 0;
		}

		// 현재 카운트
		Long currentCount = absoluteValueOfKeyword.get(absoluteValueOfKeyword.size() - 1).getAbsoluteKeywordCount();

		// 가중 평균 계산
		double weightedSum = 0;
		double weightTotal = 0;

		for (int i = absoluteValueOfKeyword.size() - movingAverageDays - 1;
			 i < absoluteValueOfKeyword.size() - 1; i++) {
			Long count = absoluteValueOfKeyword.get(i).getAbsoluteKeywordCount();
			Long prevCount = absoluteValueOfKeyword.get(i - 1).getAbsoluteKeywordCount();

			// 상승률 계산
			double percentage = (prevCount == 0) ? 0 : ((double)(count - prevCount) / prevCount) * 100;

			// 가중치를 절대값으로 설정
			weightedSum += percentage * count;
			weightTotal += count;
		}

		// 현재 상승률 계산
		Long prevCount = absoluteValueOfKeyword.get(absoluteValueOfKeyword.size() - 2).getAbsoluteKeywordCount();
		double currentPercentage = (prevCount == 0) ? 0 : ((double)(currentCount - prevCount) / prevCount) * 100;

		// 가중 평균과 현재 상승률 비교
		double weightedAverage = (weightTotal == 0) ? 0 : weightedSum / weightTotal;
		return currentPercentage - weightedAverage;
	}
}
