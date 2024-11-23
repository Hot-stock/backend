package com.bjcareer.GPTService.out.api.dto;

import lombok.Data;

@Data
public class NewsResponseDTO {
	private String link;

	public NewsResponseDTO(String link) {
		this.link = link;
	}
}
