package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public interface NewsServiceUsecase {
	List<GPTNewsDomain> findNextSchedule(String stockName, LocalDate date);
	List<GPTNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date);
	List<GPTNewsDomain> searchThemaNews(String Keyword, LocalDate date);
}
