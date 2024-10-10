package com.bjcareer.search.repository.gpt;

import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.StockRaiseReasonEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class GTPStockRepositoryAdapter {
	private final StockRaiseRepository stockRaiseRepository;

	public void save(StockRaiseReasonEntity stockRaiseReasonEntity) {
		stockRaiseRepository.save(stockRaiseReasonEntity);
	}
}
