package com.bjcareer.GPTService.in.dtos;

import java.time.LocalDate;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;

import lombok.Getter;

@Getter
public class NewsResponseDTO {
	private String title;
	private String newsLink;
	private String imgLink;
	private LocalDate pubDate;

	public NewsResponseDTO(OriginalNews news) {
		this.title = news.getTitle();
		this.newsLink = news.getNewsLink();
		this.imgLink = news.getImgLink();
		this.pubDate = news.getPubDate();
	}
}
