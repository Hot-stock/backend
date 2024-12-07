package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindRaiseReasonResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();

	public QueryToFindRaiseReasonResponseDTO(List<GPTNewsDomain> contents) {
		for (GPTNewsDomain gptNewsDomain : contents) {
			this.items.add(
				new Content(gptNewsDomain.getStockName(), gptNewsDomain.getNews().getTitle(), gptNewsDomain.getReason(),
					gptNewsDomain.getNews().getImgLink(), gptNewsDomain.getNews().getOriginalLink(),
					gptNewsDomain.getNews().getPubDate()));
		}
		this.total = items.size();
	}

	@Data
	private static class Content {
		private String stockName;
		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;

		public Content(String stockName, String title, String summary, String imgLink, String link, LocalDate date) {
			this.stockName = stockName;
			this.title = title;
			this.summary = summary;
			this.imgLink = imgLink;
			this.link = link;
			this.date = Objects.requireNonNullElseGet(date, () -> LocalDate.now(AppConfig.ZONE_ID)).toString();
		}
	}
}
