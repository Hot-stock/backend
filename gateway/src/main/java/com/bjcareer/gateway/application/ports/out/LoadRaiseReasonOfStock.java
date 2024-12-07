package com.bjcareer.gateway.application.ports.out;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadRaiseReasonOfStock {
	private final String stockCode;
	private final LocalDate date;
}
