package com.bjcareer.GPTService.in.dtos;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.Getter;

@Getter
public class ThemaResponseDTO {
	private final String name;

	public ThemaResponseDTO(GPTNewsDomain.GPTThema thema) {
		this.name = thema.getName();
	}
}
