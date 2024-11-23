package com.bjcareer.GPTService.out.api.python;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ParseNewsContentResponseDTO {
	private String title;
	private String imgLink;
	private String publishDate;
	private String text;

	public String getPublishDate() {
		if (publishDate == null || publishDate.isEmpty()) {
			return LocalDateTime.now()
				.atZone(ZoneId.of("GMT"))
				.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH));
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
