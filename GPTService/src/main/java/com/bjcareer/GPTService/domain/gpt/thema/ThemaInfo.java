package com.bjcareer.GPTService.domain.gpt.thema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ThemaInfo {
	private String name;
	private String reason;

	public ThemaInfo(String name, String reason) {
		this.name = name;
		this.reason = reason;
	}
}
