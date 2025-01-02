package com.bjcareer.GPTService.out.api.dto;

import java.time.LocalDate;
import java.time.ZoneOffset;

import lombok.Data;

@Data
public class NewsResponseDTO {
	private String link;
	private LocalDate date;

	public NewsResponseDTO(String link) {
		this.link = link;
	}

	public LocalDate getDate() {
		return date.atStartOfDay().atOffset(ZoneOffset.UTC).toLocalDate();
	}
}
