package com.bjcareer.search.out.api.gpt.news;

import com.bjcareer.search.out.api.gpt.JsonSchema;
import com.bjcareer.search.out.api.gpt.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTResponseNewsFormatDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("stock_rise_reason",
		new Schema(NewsProperties.required, new NewsProperties()), true);
}
