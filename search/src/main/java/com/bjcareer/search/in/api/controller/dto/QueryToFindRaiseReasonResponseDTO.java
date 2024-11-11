package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.bjcareer.search.domain.gpt.GTPNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindRaiseReasonResponseDTO {
	private int total;
	private List<Content> content = new ArrayList<>();

	public QueryToFindRaiseReasonResponseDTO(List<GTPNewsDomain> contents) {
		for (GTPNewsDomain gtpNewsDomain : contents) {
			this.content.add(new Content(gtpNewsDomain.getStockName(), gtpNewsDomain.getReason(),
				gtpNewsDomain.getThemas().keySet(), gtpNewsDomain.getNext().get(), gtpNewsDomain.getNextReason(),
				gtpNewsDomain.getNews().getPubDate(), gtpNewsDomain.getNews().getLink()));
		}

		content.sort(Comparator.comparing(a -> a.pubDate));
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String reason;
		private Set<String> thema;
		private LocalDate next;
		private String nextReason;
		private LocalDate pubDate;
		private String link;

		public Content(String stockName, String reason, Set<String> thema, LocalDate next, String nextReason,
			LocalDate pubDate, String link) {
			this.stockName = stockName;
			this.reason = reason;
			this.thema = thema;
			this.next = next;
			this.nextReason = nextReason;
			this.pubDate = pubDate;
			this.link = link;
		}
	}
}
