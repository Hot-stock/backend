package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties
public class NextStockEventNewsDTO {
	private long total;
	private List<EventDTO> items;

	@Getter
	@JsonIgnoreProperties
	private static class EventDTO {
		private String title;
		private String stockName;
		private String stockCode;
		private String summary;
		private String imgLink;
		private String date;
		private String nextDate;
		private String nextReason;
		private String logoLink;
		private List<String> themas;
	}
}
