package com.bjcareer.GPTService.in.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingStocksDTO {
	private List<String> rankingStocks = new ArrayList<>();

	public RankingStocksDTO(List<String> rankingStocks) {
		this.rankingStocks = rankingStocks;
	}
}
