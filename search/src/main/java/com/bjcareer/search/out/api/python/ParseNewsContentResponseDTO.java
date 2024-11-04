package com.bjcareer.search.out.api.python;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParseNewsContentResponseDTO {
	@JsonAlias("publish_date")
	private String publishDate;
	private String text;

	public String getPublishDate() {
		if (publishDate == null) {
			return "";
		}
		return publishDate;
	}

	public String getText() {
		if (text == null) {
			return "";
		}
		return text;
	}
}
