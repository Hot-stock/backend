package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface NewsServiceUsecase {
	Map<LocalDate, GTPNewsDomain> findNextSchedule(String stockName);
	Optional<GTPNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date);
}
