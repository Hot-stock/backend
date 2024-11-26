package com.bjcareer.GPTService.in.dtos;

import java.util.List;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.Getter;

@Getter
public class ListStockNewsResponseDTO {
	private final String stockName;
	private final List<RaiseStockReasonDTO> result;

	public ListStockNewsResponseDTO(String stockName, List<GPTNewsDomain> raiseReasons) {
		this.stockName = stockName;
		this.result = raiseReasons.stream()
			.map(newDomain -> new RaiseStockReasonDTO(new NewsResponseDTO(newDomain.getNews()), newDomain))
			.toList();
	}
}
