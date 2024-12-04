package com.bjcareer.gateway.out.api.search.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
public class RaiseReasonResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();

	@Data
	private static class Content {
		private String stockName;
		private String summary;
		private String imgLink;
		private String link;
	}
}
