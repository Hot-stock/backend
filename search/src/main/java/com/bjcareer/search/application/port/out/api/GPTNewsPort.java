package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.util.Optional;

import com.bjcareer.search.domain.GTPNewsDomain;

public interface GPTNewsPort {
	Optional<GTPNewsDomain> findStockRaiseReason(String message, String name, LocalDate pubDate);
}
