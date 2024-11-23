package com.bjcareer.GPTService.out.api.gpt;

public class PropertyArrayObject {
	public String type = "array";
	public String description;
	public PropertyObject items;

	public PropertyArrayObject(String description, PropertyObject items) {
		this.description = description;
		this.items = items;
	}
}
