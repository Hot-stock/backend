package com.bjcareer.search.application.search;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.in.SearchUsecase;
import com.bjcareer.search.application.port.out.persistence.stock.LoadStockCommand;
import com.bjcareer.search.application.port.out.persistence.stock.LoadStockRaiseReason;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaUsingkeywordCommand;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.NewsHelper;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
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
		Optional<Stock> byCode = stockRepositoryPort.findByCode(keyword);
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
	public List<GPTStockNewsDomain> findRaiseReason(String stockCode, LocalDate date) {
		Map<LocalDate, List<GPTStockNewsDomain>> newsMap = new HashMap<>();
		Optional<Stock> byCode = stockRepositoryPort.findByCode(stockCode);

		if (byCode.isEmpty()) {
			return List.of();
		}

		LoadStockRaiseReason command = new LoadStockRaiseReason(byCode.get().getName(), date);
		List<GPTStockNewsDomain> raiseReason = documentAnalyzeRepository.getRaiseReason(command);
		raiseReason = NewsHelper.RemoveDuplicatedNews(raiseReason);

		return raiseReason;
	}

	@Override
	public Pair<List<String>, List<GPTThemaNewsDomain>> findThemasNews(String stockCode, String themaName, LocalDate date) {
		Optional<Stock> byCode = stockRepositoryPort.findByCode(stockCode);

		List<String> target = new ArrayList<>();
		List<GPTThemaNewsDomain> result = new ArrayList<>();

		if (themaName.equals("ALL")) {
			Stock stock = byCode.get();
			target = stock.getThemas().stream().map(t -> t.getThemaInfo().getName()).toList();
		} else {
			target.add(themaName);
		}

		target.stream().map(thema -> documentAnalyzeRepository.getThemaNews(new LoadThemaNewsCommand(thema, date)))
			.forEach(result::addAll);

		List<String> theams = byCode.get().getThemas().stream().map(t -> t.getThemaInfo().getName()).toList();
		return Pair.of(theams, result);
	}

	@Override
	public List<String> getSuggestionKeyword(String keyword) {
		return trie.search(keyword);
	}
}
