package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindNextEventReasonResponseDTO {
	private int total;
	private List<Content> content = new ArrayList<>();

	public QueryToFindNextEventReasonResponseDTO(List<GPTNewsDomain> contents) {
		for (GPTNewsDomain GPTNewsDomain : contents) {
			this.content.add(new Content(GPTNewsDomain.getStockName(), GPTNewsDomain.getNextReason(),
				GPTNewsDomain.getNews().getImgLink(), GPTNewsDomain.getNews().getOriginalLink(),
				GPTNewsDomain.getNext().get()));
		}
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String nextEventReason;
		private String imgLink;
		private String link;
		private LocalDate nextEventDate;

		public Content(String stockName, String nextEventReason, String imgLink, String link, LocalDate nextEventDate) {
			this.stockName = stockName;
			this.nextEventReason = nextEventReason;
			this.imgLink = imgLink;
			this.link = link;
			this.nextEventDate = nextEventDate;
		}
	}
}
