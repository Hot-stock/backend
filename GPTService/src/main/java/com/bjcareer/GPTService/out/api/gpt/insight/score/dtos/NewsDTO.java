package com.bjcareer.GPTService.out.api.gpt.insight.score.dtos;

public class NewsDTO {
	public String pubDate;
	public String news;
	public String nextEvent;

	public NewsDTO(String pubDate, String news, String nextEvent) {
		this.pubDate = pubDate;
		this.news = news;
		this.nextEvent = nextEvent;
	}
}
