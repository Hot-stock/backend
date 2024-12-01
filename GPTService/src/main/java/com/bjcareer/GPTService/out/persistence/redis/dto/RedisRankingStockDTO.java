package com.bjcareer.GPTService.out.persistence.redis.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedisRankingStockDTO {
	private final String stockName;
	private final String title;
	private final String summary;
	private final String newsURL;
	private final String imageURL;
}
