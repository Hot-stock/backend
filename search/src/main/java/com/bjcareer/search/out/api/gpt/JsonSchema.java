package com.bjcareer.search.out.api.gpt;

import lombok.Getter;

@Getter
public class JsonSchema {
	private final String name;
	private final Schema schema;
	public boolean strict = true;

	public JsonSchema(String name, Schema schema, boolean strict) {
		this.name = name;
		this.schema = schema;
	}
}
