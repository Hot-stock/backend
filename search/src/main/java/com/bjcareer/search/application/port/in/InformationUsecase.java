package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.Map;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public interface InformationUsecase {
	Map<LocalDate, GPTNewsDomain> findSearchRaiseReason(String stockName);
}
