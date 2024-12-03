package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.util.Optional;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public interface GPTNewsPort {
	Optional<GPTNewsDomain> findStockRaiseReason(String message, String name, LocalDate pubDate);
}
