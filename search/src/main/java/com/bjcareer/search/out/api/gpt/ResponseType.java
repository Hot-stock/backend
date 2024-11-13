package com.bjcareer.search.out.api.gpt;

public class ResponseType {
	private final String type = "json_schema";
	private final JsonSchema json_schema;

	public ResponseType(JsonSchema json_schema) {
		this.json_schema = json_schema;
	}
}
