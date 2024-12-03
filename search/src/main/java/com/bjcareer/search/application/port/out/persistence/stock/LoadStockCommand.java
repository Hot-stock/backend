package com.bjcareer.search.application.port.out.persistence.stock;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoadStockCommand {
	private final String keyword;
}
