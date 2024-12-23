package com.bjcareer.search.out.api.toss;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.out.api.toss.dtos.CandleResponseDTO;
import com.bjcareer.search.out.api.toss.dtos.TossStockInfoDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TossServerAdapter {
	private String CANDLE_URI = "https://wts-info-api.tossinvest.com/api/v2/stock-prices/A%s/period-candles/%s:1?count=365";
	private String STOCK_INFO = "https://wts-info-api.tossinvest.com/api/v2/stock-infos/A%s";
	private final WebClient webClient;

	public CandleResponseDTO getStockPriceURI(String stockCode, String period) {
		return webClient.get()
			.uri(String.format(CANDLE_URI, stockCode, period))
			.retrieve()
			.bodyToMono(CandleResponseDTO.class)
			.block();
	}

	public TossStockInfoDTO getStockInfo(String stockCode) {
		return webClient.get()
			.uri(String.format(STOCK_INFO, stockCode))
			.retrieve()
			.bodyToMono(TossStockInfoDTO.class)
			.block();
	}
}
