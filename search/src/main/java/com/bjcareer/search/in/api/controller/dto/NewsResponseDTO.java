package com.bjcareer.search.in.api.controller.dto;

import java.time.LocalDate;

import com.bjcareer.search.domain.News;

import lombok.Getter;

@Getter
public class NewsResponseDTO {
	private String title;
	private String imgLink;
	private String link;
	private String date;

	public NewsResponseDTO(News news) {
		this.title = news.getTitle();
		this.imgLink = news.getImgLink();
		this.link = news.getOriginalLink();
		this.date = news.getPubDate().toString();
	}
}
