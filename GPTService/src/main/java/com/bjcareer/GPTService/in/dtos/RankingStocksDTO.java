package com.bjcareer.GPTService.in.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingStocksDTO {
	private List<String> rankingStocks = new ArrayList<>();
	private LocalDate baseAt;

	public RankingStocksDTO(List<String> rankingStocks, LocalDate baseAt) {
		this.rankingStocks = rankingStocks;
		this.baseAt = baseAt;
	}
}
