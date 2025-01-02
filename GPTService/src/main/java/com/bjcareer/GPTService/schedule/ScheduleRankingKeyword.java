package com.bjcareer.GPTService.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.AnalyzeInsightKeyword;
import com.bjcareer.GPTService.application.AnalyzeRankingStock;
import com.bjcareer.GPTService.domain.Stock;
import com.bjcareer.GPTService.out.api.python.AbsoluteRankKeywordDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.rdb.StockRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisTrendKeywordRankAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRankingKeyword {
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final RedisTrendKeywordRankAdapter trendKeywordRankAdapter;
	private final RedisThemaRepository redisThemaRepository;
	private final AnalyzeRankingStock analyzeRankingStock;
	private final StockRepository stockRepository;
	private final AnalyzeInsightKeyword analyzeInsightKeyword;

	@Scheduled(cron = "0 50 8 * * *", zone = "Asia/Seoul")
	public void updateRankingKeyword() {
		log.info("Start update ranking keyword");

		List<Stock> all = stockRepository.findAll();

		for (Stock stock : all) {
			List<AbsoluteRankKeywordDTO> absoluteValueOfKeyword = pythonSearchServerAdapter.loadRankKeyword(
				stock.getName());

			double percentage = getWeightedPercentageWithMovingAverage(absoluteValueOfKeyword, 3);
			log.debug("Start update ranking keyword for stock: {} Percentage: {}", stock.getName(), percentage);
			trendKeywordRankAdapter.updateRanking(stock.getName(), percentage,
				RedisTrendKeywordRankAdapter.STOCK_RANK_BUCKET);

			// if (percentage > -400) {
			// 	log.debug("Start update insight keyword for stock: {}", stock.getName());
			// 	analyzeInsightKeyword.analyzeInsightKeyword(stock.getName(), LocalDate.now().minusDays(1),
			// 		LocalDate.now());
			// }
		}

		analyzeRankingStock.analyzeRankingStock();
		log.info("End update ranking keyword");
	}

	@Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
	public void updateRankingThemaKeyword() {
		log.info("Start update thema ranking keyword");
		List<String> strings = redisThemaRepository.loadThema();

		for (String thema : strings) {
			List<AbsoluteRankKeywordDTO> absoluteValueOfKeyword = pythonSearchServerAdapter.loadRankKeyword(thema);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.warn("Failed to sleep thread {} {}", e, thema);
			}

			double percentage = getWeightedPercentageWithMovingAverage(absoluteValueOfKeyword, 3);
			log.debug("Start update ranking keyword for stock: {} Percentage: {}", thema, percentage);
			trendKeywordRankAdapter.updateRanking(thema, percentage + 0.0, RedisTrendKeywordRankAdapter.THEMA_RANK_BUCKET);
		}

		log.info("End update ranking keyword");
	}

	private double getWeightedPercentageWithMovingAverage(List<AbsoluteRankKeywordDTO> absoluteValueOfKeyword,
		int movingAverageDays) {
		// 데이터가 충분하지 않으면 0 반환
		if (absoluteValueOfKeyword.size() < movingAverageDays) {
			return 0;
		}

		// 현재 카운트
		log.info("{} {}", absoluteValueOfKeyword.size(), movingAverageDays);
		Long currentCount = absoluteValueOfKeyword.get(absoluteValueOfKeyword.size() - 1).getAbsoluteKeywordCount();
		List<AbsoluteRankKeywordDTO> absoluteRankKeywordDTOS = absoluteValueOfKeyword.subList(absoluteValueOfKeyword.size() - movingAverageDays, absoluteValueOfKeyword.size() - 1);
		// 가중 평균 계산
		double weightedSum = 0;
		double weightTotal = 0;

		for (AbsoluteRankKeywordDTO keywordDTO : absoluteRankKeywordDTOS) {
			Long count = keywordDTO.getAbsoluteKeywordCount();
			Long prevCount = keywordDTO.getAbsoluteKeywordCount();

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
