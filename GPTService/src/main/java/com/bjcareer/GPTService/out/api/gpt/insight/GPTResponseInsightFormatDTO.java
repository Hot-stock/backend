package com.bjcareer.GPTService.out.api.gpt.insight;

import com.bjcareer.GPTService.out.api.gpt.JsonSchema;
import com.bjcareer.GPTService.out.api.gpt.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTResponseInsightFormatDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("stock_insight_time_travel_style",
		new Schema(InsightProperties.required, new InsightProperties()), true);
}
