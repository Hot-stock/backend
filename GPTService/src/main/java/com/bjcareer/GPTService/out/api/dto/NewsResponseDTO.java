package com.bjcareer.GPTService.out.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NewsResponseDTO {
	private String link;
	private LocalDate date;

	public NewsResponseDTO(String link) {
		this.link = link;
	}
}
