package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import com.bjcareer.GPTService.out.api.gpt.JsonSchema;
import com.bjcareer.GPTService.out.api.gpt.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTResponseThemaFormatDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("thema_news_analysis",
		new Schema(ThemaProperties.required, new ThemaProperties()), true);
}
