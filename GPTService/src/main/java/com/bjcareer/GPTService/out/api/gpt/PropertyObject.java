package com.bjcareer.GPTService.out.api.gpt;

public class PropertyObject {
	public String type = "object";
	public Object properties;
	public boolean additionalProperties = false;
	public String[] required;

	public PropertyObject(Object properties, String[] required) {
		this.properties = properties;
		this.required = required;
	}
}
