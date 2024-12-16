package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindNextEventReasonResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();

	public QueryToFindNextEventReasonResponseDTO(List<GPTStockNewsDomain> contents) {
		for (GPTStockNewsDomain GPTStockNewsDomain : contents) {
			this.items.add(new Content(GPTStockNewsDomain.getStockName(), GPTStockNewsDomain.getNextReason(),
				GPTStockNewsDomain.getNews().getImgLink(), GPTStockNewsDomain.getNews().getOriginalLink(),
				GPTStockNewsDomain.getNext().get()));
		}
		this.total = items.size();
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
