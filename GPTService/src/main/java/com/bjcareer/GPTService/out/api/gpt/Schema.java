package com.bjcareer.GPTService.out.api.gpt;

import lombok.Getter;

@Getter
public class Schema {
	private final String type = "object";
	private final String[] required;
	private final boolean additionalProperties = false;
	private final Object properties;

	public Schema(String[] required, Object properties) {
		this.required = required;
		this.properties = properties;
	}
}
