package com.bjcareer.search.out.api.dto;

import lombok.Data;

@Data
public class NewsItemDTO {
	private String title;
	private String originalLink;
	private String link;
	private String description;
	private String pubDate;
}
