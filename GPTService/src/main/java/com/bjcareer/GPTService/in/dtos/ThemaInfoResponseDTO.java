package com.bjcareer.GPTService.in.dtos;

import lombok.Getter;

@Getter
public class ThemaInfoResponseDTO {
	private String name;
	private String reason;

	public ThemaInfoResponseDTO(String name, String reason) {
		this.name = name;
		this.reason = reason;
	}
}
