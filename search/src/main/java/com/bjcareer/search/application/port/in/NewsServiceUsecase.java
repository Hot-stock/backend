package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public interface NewsServiceUsecase {
	List<GPTStockNewsDomain> findNextSchedule(String stockName, LocalDate date);
	List<GPTStockNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date);
	List<GPTStockNewsDomain> searchThemaNews(String Keyword, LocalDate date);
}
