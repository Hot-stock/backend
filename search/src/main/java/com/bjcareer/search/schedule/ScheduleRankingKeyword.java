package com.bjcareer.search.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.search.ConverterKeywordCountService;
import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.out.persistence.cache.CacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRankingKeyword {
	private final ConverterKeywordCountService converterKeywordCountService;
	private final StockRepositoryPort stockRepositoryPort;
	private final CacheRepository cacheRepository;

	@Scheduled(fixedDelay = 86400000)
	public void updateRankingKeyword() {
		log.info("Start update ranking keyword");

		List<Stock> stocks = stockRepositoryPort.findAll();

		for (Stock stock : stocks) {
			List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterKeywordCountService.getAbsoluteValueOfKeyword(
				stock.getName());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			double percentage = getPercentage(absoluteValueOfKeyword);
			cacheRepository.updateRanking(stock.getName(), percentage);
		}
		log.info("End update ranking keyword");
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
