package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
				gtpNewsDomain.getThemas().stream().map(s -> s.getName()).toList(), gtpNewsDomain.getNews().getPubDate(),
				gtpNewsDomain.getNews().getLink()));
		}

		content.sort(Comparator.comparing(a -> a.pubDate));
		this.total = content.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String reason;
		private List<String> thema;
		private LocalDate pubDate;
		private String link;

		public Content(String stockName, String reason, List<String> thema, LocalDate pubDate, String link) {
			this.stockName = stockName;
			this.reason = reason;
			this.thema = thema;
			this.pubDate = pubDate;
			this.link = link;
		}
	}
}
