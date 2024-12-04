package com.bjcareer.gateway.application.ports.out;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindRaiseReasonOfStock {
	private final String stockName;
	private final LocalDate date;
}
