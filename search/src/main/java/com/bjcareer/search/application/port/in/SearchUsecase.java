package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.util.Pair;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

public interface SearchUsecase {
	List<Thema> filterThemesByQuery(String keyword);
	List<Stock> filterStockByQuery(String keyword);
	List<Stock> getSuggestionStocks();
	List<GPTStockNewsDomain> findRaiseReason(String stockName, LocalDate date);
	Pair<List<String>, List<GPTThemaNewsDomain>> findThemasNews(String code, String themaName, LocalDate date);
	List<String> getSuggestionKeyword(String keyword);
}
