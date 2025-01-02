package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName.dtos;

import com.bjcareer.GPTService.out.api.gpt.JsonSchema;
import com.bjcareer.GPTService.out.api.gpt.Schema;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName.ThemaNameProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTResponseThemaNameOfThemaFormatDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("thema_name",
		new Schema(ThemaNameProperties.required, new ThemaNameProperties()), true);
}
