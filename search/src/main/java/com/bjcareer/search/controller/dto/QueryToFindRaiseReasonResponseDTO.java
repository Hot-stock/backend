package com.bjcareer.search.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.GTPNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindRaiseReasonResponseDTO {
	private int total;
	private List<Content> content = new ArrayList<>();

	public QueryToFindRaiseReasonResponseDTO(Map<LocalDate, GTPNewsDomain> contents) {
		for (LocalDate date : contents.keySet()) {
			GTPNewsDomain gtpNewsDomain = contents.get(date);
			this.content.add(new Content(gtpNewsDomain.getStockName(), gtpNewsDomain.getReason(),
				gtpNewsDomain.getThema(), gtpNewsDomain.getNext(), gtpNewsDomain.getNextReason(), gtpNewsDomain.getNewsDomain().getPubDate()));
		}

		content.sort(Comparator.comparing(a -> a.pubDate));
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String reason;
		private String thema;
		private LocalDate next;
		private String nextReason;
		private LocalDate pubDate;

		public Content(String stockName, String reason, String thema, LocalDate next, String nextReason, LocalDate pubDate) {
			this.stockName = stockName;
			this.reason = reason;
			this.thema = thema;
			this.next = next;
			this.nextReason = nextReason;
			this.pubDate = pubDate;
		}
	}
}
