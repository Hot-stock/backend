package com.bjcareer.search.out.api.gpt;

import com.bjcareer.search.out.api.gpt.insight.GPTResponseInsightFormatDTO;

public class PropertyArrayDetail {
	public String type = "array";
	public String description;
	public Items items;

	public PropertyArrayDetail(String description, Items items) {
		this.type = type;
		this.description = description;
		this.items = items;
	}

	public static class Items {
		public String type;

		public Items(String type) {
			this.type = type;
		}
	}

}
