package com.bjcareer.GPTService.out.api.gpt;

public class PropertyArrayDetail {
	public String type = "array";
	public String description;
	public Object items;

	public PropertyArrayDetail(String description, Object items) {
		this.type = type;
		this.description = description;
		this.items = items;
	}
}
