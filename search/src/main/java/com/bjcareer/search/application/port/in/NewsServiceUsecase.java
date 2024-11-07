package com.bjcareer.search.application.port.in;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface NewsServiceUsecase {
	List<GTPNewsDomain> findNextSchedule(String stockName, LocalDate date);

	List<GTPNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date);
}
