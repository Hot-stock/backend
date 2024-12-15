package com.bjcareer.search.schedule;

import java.util.HashMap;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.application.search.ConverterKeywordCountService;
import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.persistence.cache.CacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRankingKeyword {
	private final ConverterKeywordCountService converterKeywordCountService;
	private final CacheRepository cacheRepository;

	private final StockRepositoryPort stockRepositoryPort;
	private final ThemaInfoRepositoryPort themaInfoRepositoryPort;

	@Scheduled(cron = "0 0 10 * * *")
	public void updateRankingKeyword() {
		log.info("Start update ranking keyword");

		List<Stock> stocks = stockRepositoryPort.findAll();
		List<ThemaInfo> all = themaInfoRepositoryPort.findAll();

		HashMap<String, Double> themaScore = getThemaScore(all);

		for (Stock stock : stocks) {
			List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterKeywordCountService.getAbsoluteValueOfKeyword(
				stock.getName());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			List<Thema> themas = stock.getThemas();
			double themaScoreValue = 0;

			for (Thema thema : themas) {
				themaScoreValue += themaScore.get(thema.getThemaInfo().getName());
			}

			double percentage = getPercentage(absoluteValueOfKeyword);
			cacheRepository.updateRanking(stock.getName(), percentage + themaScoreValue);
		}

		log.info("End update ranking keyword");
	}

	private HashMap<String, Double> getThemaScore(List<ThemaInfo> all) {
		HashMap<String, Double> themaScore = new HashMap<>();

		for (ThemaInfo themaInfo : all) {
			List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterKeywordCountService.getAbsoluteValueOfKeyword(
				themaInfo.getName());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			double percentage = getPercentage(absoluteValueOfKeyword);
			cacheRepository.updateRanking(themaInfo.getName(), percentage);
		}

		return themaScore;
	}

	private double getPercentage(List<AbsoluteRankKeyword> absoluteValueOfKeyword) {
		if (absoluteValueOfKeyword.size() < 2) {
			return 0;
		}

		Long currentCount = absoluteValueOfKeyword.getLast().getAbsoluteKeywordCount();
		Long previousCount = absoluteValueOfKeyword.get(absoluteValueOfKeyword.size() - 2).getAbsoluteKeywordCount();

		return ((double)(currentCount - previousCount) / previousCount) * 100;
	}
}
