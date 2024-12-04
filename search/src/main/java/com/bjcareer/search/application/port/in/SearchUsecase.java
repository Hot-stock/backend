package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public interface SearchUsecase {
	List<Thema> filterThemesByQuery(String keyword);
	List<Stock> filterStockByQuery(String keyword);
	List<GPTNewsDomain> findRaiseReason(String stockName, LocalDate date);
	List<String> getSuggestionKeyword(String keyword);
}
