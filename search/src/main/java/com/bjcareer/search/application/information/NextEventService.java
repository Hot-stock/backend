package com.bjcareer.search.application.information;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NextEventService {
	private final DocumentAnalyzeRepository documentAnalyzeRepository;
	private final StockRepositoryPort stockRepositoryPort;

	public List<GPTStockNewsDomain> getUpcomingEvents(){
		return documentAnalyzeRepository.getUpcomingNews();
	}

	public List<GPTStockNewsDomain> filterUpcomingEventsByStockName(String stockCode) {
		Optional<Stock> byCode =
			stockRepositoryPort.findByCode(stockCode);
		if (byCode.isEmpty()) {
			return List.of();
		}

		return documentAnalyzeRepository.getUpcomingNewsByStockName(byCode.get().getName());
	}
}
