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
		for (GPTNewsDomain gptNewsDomain : contents) {
			this.content.add(
				new Content(gptNewsDomain.getStockName(), gptNewsDomain.getReason(), gptNewsDomain.getNextReason(),
					gptNewsDomain.getNews().getImgLink(), gptNewsDomain.getNews().getOriginalLink()));
		}
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String summary;
		private String nextEventReason;
		private String imgLink;
		private String link;

		public Content(String stockName, String summary, String nextEventReason, String imgLink, String link) {
			this.stockName = stockName;
			this.summary = summary;
			this.nextEventReason = nextEventReason;
			this.imgLink = imgLink;
			this.link = link;
		}
	}
}
