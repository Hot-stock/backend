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
import com.bjcareer.search.out.persistence.cache.CacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService implements RankingUsecase {
	private final CacheRepository cacheRepository;
	private final StockRepositoryPort stockRepositoryPort;
	private final S3Service s3Service;

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

	private void setURLToStockDomain(Pair<String, Double> t, List<Pair<Stock, String>> result) {
		Optional<Stock> byName = stockRepositoryPort.findByName(t.getFirst());
		Stock stock = byName.get();
		stock.setPreSignedURL(s3Service.getStockLogoURL(stock.getName()));
		result.add(Pair.of(stock, t.getSecond().toString()));
	}
}
