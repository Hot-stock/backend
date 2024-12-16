package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryToFindThemaNewsResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();
	private List<String> themas = new ArrayList<>();

	public QueryToFindThemaNewsResponseDTO(List<GPTThemaNewsDomain> contents, List<String> themas) {
		for (GPTThemaNewsDomain gptThemaNewsDomainDomain : contents) {
			this.items.add(
				new Content(gptThemaNewsDomainDomain.getName(), gptThemaNewsDomainDomain.getNews().getTitle(), gptThemaNewsDomainDomain.getSummary(),
					gptThemaNewsDomainDomain.getNews().getImgLink(), gptThemaNewsDomainDomain.getNews().getOriginalLink(),
					gptThemaNewsDomainDomain.getNews().getPubDate()));
		}
		this.themas = themas;
		this.total = items.size();
	}

	@Data
	private static class Content {
		private String name;
		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;

		public Content(String name, String title, String summary, String imgLink, String link, LocalDate date) {
			this.name = name;
			this.title = title;
			this.summary = summary;
			this.imgLink = imgLink;
			this.link = link;
			this.date = Objects.requireNonNullElseGet(date, () -> LocalDate.now(AppConfig.ZONE_ID)).toString();
		}
	}
}
