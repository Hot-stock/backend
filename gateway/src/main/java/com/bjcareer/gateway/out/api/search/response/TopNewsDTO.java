package com.bjcareer.gateway.out.api.search.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TopNewsDTO {
	private Integer total;
	private List<RaiseReasonDTO> items;

	@Getter
	@ToString
	static class RaiseReasonDTO {
		private String stockName;
		private String logoLink;
		private List<String> themas = new ArrayList<>();
		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;
	}
}
