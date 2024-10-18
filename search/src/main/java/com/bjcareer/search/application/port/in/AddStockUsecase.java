package com.bjcareer.search.application.port.in;

import com.bjcareer.search.domain.entity.Thema;

public interface AddStockUsecase {
	Thema addStockThema(String stockCode, String stockName, String themeName);
}
