package com.bjcareer.search.out.persistence.cache.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisRankingStockDTO {
	private String stockName;
	private String title;
	private String summary;
	private String newsURL;
	private String imageURL;

	public RedisRankingStockDTO(String stockName, String title, String summary, String newsURL, String imageURL) {
		this.stockName = stockName;
		this.title = title;
		this.summary = summary;
		this.newsURL = newsURL;
		this.imageURL = imageURL;
	}
}
