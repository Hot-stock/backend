package com.bjcareer.search.application.port.out.persistence.thema;

import lombok.Data;

@Data
public class LoadStockByThemaCommand {
	private String stockName;
	private String themaName;
}
