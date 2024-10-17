package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.Map;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface InformationUsecase {
	Map<LocalDate, GTPNewsDomain> findSearchRaiseReason(String stockName);
}
