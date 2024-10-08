package com.bjcareer.gateway.application.ports.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StockInfoCommand {
	private final String stockName;
	private final String thema;
	private final String code;
}
