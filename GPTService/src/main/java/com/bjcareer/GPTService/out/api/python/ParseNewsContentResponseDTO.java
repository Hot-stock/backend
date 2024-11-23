package com.bjcareer.GPTService.out.api.python;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParseNewsContentResponseDTO {
	private String title;
	private String imgLink;
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
