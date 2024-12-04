package com.bjcareer.search.application.port.out.persistence.stock;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadStockRaiseReason {
	private final String stockName;
	private final LocalDate date;
}
