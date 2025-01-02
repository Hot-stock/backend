package com.bjcareer.search.application.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.S3Service;
import com.bjcareer.search.application.port.in.RankingUsecase;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.bjcareer.search.out.persistence.cache.CacheRepository;
import com.bjcareer.search.out.persistence.cache.RedisSuggestionAdapter;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeInsightRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService implements RankingUsecase {
	private final CacheRepository cacheRepository;
	private final StockRepositoryPort stockRepositoryPort;
	private final RedisSuggestionAdapter redisSuggestionAdapter;
	private final S3Service s3Service;
	private final DocumentAnalyzeInsightRepository documentAnalyzeInsightRepository;

	public void updateKeyword(String keyword) {
		log.debug("Keyword to update is {}", keyword);
		// cacheRepository.updateRanking(keyword);
	}

	public List<Pair<Stock, String>> getRankKeyword(Integer index) {
		List<Pair<Stock, String>> result = new ArrayList<>();

		List<Pair<String, Double>> ranking = cacheRepository.getRanking(index);
		ranking.forEach(t -> setURLToStockDomain(t, result));

		return result;
	}

	@Override
	public List<GPTInsight> getSuggestionStocks() {
		List<String> suggestionStock = redisSuggestionAdapter.getSuggestionStock();

		List<GPTInsight> insights = suggestionStock.stream()
			.map(t -> documentAnalyzeInsightRepository.getInsightOfStockByLatest(t).orElseGet(() -> {
				log.warn("Insight not found: {}", t);
				return new GPTInsight(false, t, "키워드 검색량 기반 추천입니다.", "키워드 검색량이 급증한 것을 확인했으나, 추가적인 인사이트는 발견하지 못했습니다.", 0,
					null);
			}))
			.toList();

		for (GPTInsight insight : insights) {
			Optional<Stock> byName = stockRepositoryPort.findByName(insight.getStockName());

			if (byName.isEmpty()) {
				log.warn("Stock not found: {}", insight.getStockName());
				continue;
			}

			Stock stock = byName.get();
			stock.setPreSignedURL(s3Service.getStockLogoURL(stock.getName()));
			insight.addStock(stock);
		}

		return insights;
	}

	private void setURLToStockDomain(Pair<String, Double> t, List<Pair<Stock, String>> result) {
		Optional<Stock> byName = stockRepositoryPort.findByName(t.getFirst());
		if (byName.isEmpty()) {
			return;
		}
		Stock stock = byName.get();
		stock.setPreSignedURL(s3Service.getStockLogoURL(stock.getName()));
				result.add(Pair.of(stock, String.valueOf(t.getSecond().intValue() * -1)));
	}

}
