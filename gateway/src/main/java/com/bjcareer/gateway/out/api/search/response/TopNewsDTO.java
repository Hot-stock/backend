package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TopNewsDTO {
	private Integer total;
	private List<RaiseReasonDTO> content;

	@Getter
	@ToString
	static class RaiseReasonDTO {
		private String stockName;
		private String reason;
		private List<String> thema;
		private String pubDate;
		private String ling;
	}
}