package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface NewsServiceUsecase {
	Map<LocalDate, GTPNewsDomain> finNextSchedule(String stockName);
	Optional<GTPNewsDomain> findRaiseReasonThadDate(String stockName, LocalDate date);
}
