package com.bjcareer.GPTService.in.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RankingStocksDTO {
	private List<String> rankingStocks = new ArrayList<>();
}
