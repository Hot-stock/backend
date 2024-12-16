package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.util.Optional;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public interface GPTNewsPort {
	Optional<GPTStockNewsDomain> findStockRaiseReason(String message, String name, LocalDate pubDate);
}
