package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindRaiseReasonResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();

	public QueryToFindRaiseReasonResponseDTO(List<GPTStockNewsDomain> contents) {
		for (GPTStockNewsDomain gptStockNewsDomain : contents) {
			this.items.add(new Content(gptStockNewsDomain));
		}
		this.total = items.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String logoLink;
		private List<String> themas = new ArrayList<>();

		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;

		public Content(GPTStockNewsDomain gptStockNewsDomain) {
			this.stockName = gptStockNewsDomain.getStockName();
			this.title = gptStockNewsDomain.getNews().getTitle();
			this.summary = gptStockNewsDomain.getReason();
			this.themas = gptStockNewsDomain.getThemas();
			this.imgLink = gptStockNewsDomain.getNews().getImgLink();
			this.link = gptStockNewsDomain.getNews().getOriginalLink();
			this.logoLink = gptStockNewsDomain.getPreSignedStockLogoUrl();
			this.date = Objects.requireNonNullElseGet(gptStockNewsDomain.getNews().getPubDate(),
				() -> LocalDate.now(AppConfig.ZONE_ID)).toString();
		}
	}
}
