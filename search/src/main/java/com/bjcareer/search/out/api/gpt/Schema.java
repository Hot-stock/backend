package com.bjcareer.search.out.api.gpt;

import com.bjcareer.search.out.api.gpt.insight.InsightProperties;

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
