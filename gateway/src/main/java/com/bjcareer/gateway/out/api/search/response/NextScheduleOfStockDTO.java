package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Getter;

@Getter
public class NextScheduleOfStockDTO {
	private Integer total;
	private List<NextScheduleOfStockContentDTO> content;

	@Getter
	static class NextScheduleOfStockContentDTO {
		private String stockName;
		private String reason;
		private String thema;
		private String next;
		private String nextReason;
		private String pubDate;
	}
}
