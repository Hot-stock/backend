package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.Map;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public interface InformationUsecase {
	Map<LocalDate, GPTStockNewsDomain> findSearchRaiseReason(String stockName);
}
