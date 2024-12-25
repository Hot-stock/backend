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
public class QueryStockNewsResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();

	public QueryStockNewsResponseDTO(List<GPTStockNewsDomain> contents) {
		for (GPTStockNewsDomain gptStockNewsDomain : contents) {
			this.items.add(new Content(gptStockNewsDomain));
		}
		this.total = items.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String stockCode;
		private String logoLink;
		private List<String> themas;

		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;
		private String nextDate;
		private String nextReason;

		public Content(GPTStockNewsDomain gptStockNewsDomain) {
			this.stockName = gptStockNewsDomain.getStockName();
			this.stockCode = gptStockNewsDomain.getStockCode();
			this.title = gptStockNewsDomain.getNews().getTitle();
			this.summary = gptStockNewsDomain.getReason();
			this.themas = gptStockNewsDomain.getThemas();
			this.imgLink = gptStockNewsDomain.getNews().getImgLink();
			this.link = gptStockNewsDomain.getNews().getOriginalLink();
			this.logoLink = gptStockNewsDomain.getPreSignedStockLogoUrl();
			this.date = Objects.requireNonNullElseGet(gptStockNewsDomain.getNews().getPubDate(),
				() -> LocalDate.now(AppConfig.ZONE_ID)).toString();
			this.nextDate = gptStockNewsDomain.getNext().isEmpty() ? null : gptStockNewsDomain.getNext().get().toString();
			this.nextReason = gptStockNewsDomain.getNextReason();

		}
	}
}
