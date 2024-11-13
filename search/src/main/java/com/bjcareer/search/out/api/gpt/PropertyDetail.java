package com.bjcareer.search.out.api.gpt;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PropertyDetail {
	public String type;
	public String description;

	public PropertyDetail(String type, String description) {
		this.type = type;
		this.description = description;
	}
}
