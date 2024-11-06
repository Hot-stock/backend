package com.bjcareer.search.application.port.out.persistence.thema;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoadStockByThemaCommand {
	private final String stockName;
	private final String themaName;
}
