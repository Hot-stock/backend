package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import com.bjcareer.GPTService.out.api.gpt.JsonSchema;
import com.bjcareer.GPTService.out.api.gpt.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTTriggerFormatResponseDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("stock_time_travel_style",
		new Schema(TriggerProperties.required, new TriggerProperties()), true);
}
