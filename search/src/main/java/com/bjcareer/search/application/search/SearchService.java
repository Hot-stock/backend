package com.bjcareer.search.application.search;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.in.SearchUsecase;
import com.bjcareer.search.application.port.out.persistence.stock.LoadStockCommand;
import com.bjcareer.search.application.port.out.persistence.stock.LoadStockRaiseReason;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaUsingkeywordCommand;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.event.SearchedKeyword;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUsecase {
	private final ApplicationEventPublisher eventPublisher;
	private final ThemaRepositoryPort themaRepositoryPort;
	private final StockRepositoryPort stockRepositoryPort;
	private final DocumentAnalyzeRepository documentAnalyzeRepository;
	private final Trie trie;

	public List<Thema> filterThemesByQuery(String keyword) {
		LoadThemaUsingkeywordCommand command = new LoadThemaUsingkeywordCommand(keyword);
		List<Thema> resultOfSearch = themaRepositoryPort.loadAllByKeywordContaining(command);

		if (!resultOfSearch.isEmpty()) {
			eventPublisher.publishEvent(new SearchedKeyword(keyword));
		}
		return resultOfSearch;
	}

	@Override
	public List<Stock> filterStockByQuery(String keyword) {
		LoadStockCommand command = new LoadStockCommand(keyword);
		return stockRepositoryPort.loadAllByKeywordContaining(command);
	}

	@Override
	public List<GPTNewsDomain> findRaiseReason(String stockName, LocalDate date) {
		LoadStockRaiseReason command = new LoadStockRaiseReason(stockName, date);
		List<GPTNewsDomain> raiseReason = documentAnalyzeRepository.getRaiseReason(command);
		return raiseReason;
	}

	@Override
	public List<String> getSuggestionKeyword(String keyword) {
		return trie.search(keyword);
	}
}
