package com.bjcareer.search.in.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindRaiseReasonResponseDTO {
	private int total;
	private List<Content> content = new ArrayList<>();

	public QueryToFindRaiseReasonResponseDTO(List<GPTNewsDomain> contents) {
		for (GPTNewsDomain GPTNewsDomain : contents) {
			this.content.add(new Content(GPTNewsDomain.getStockName(), GPTNewsDomain.getReason(),
				GPTNewsDomain.getNews().getImgLink(), GPTNewsDomain.getNews().getOriginalLink()));
		}
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String reason;
		private String imgLink;
		private String link;

		public Content(String stockName, String reason, String imgLink, String link) {
			this.stockName = stockName;
			this.reason = reason;
			this.imgLink = imgLink;
			this.link = link;
		}
	}
}
