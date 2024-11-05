package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface NewsServiceUsecase {
	List<GTPNewsDomain> findNextSchedule(String stockName, LocalDate date);
	Optional<GTPNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date);
}
