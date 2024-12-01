package com.bjcareer.search.in.api.controller.dto;

import java.util.ArrayList;
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
				gtpNewsDomain.getNews().getImgLink(), gtpNewsDomain.getNews().getOriginalLink()));
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
