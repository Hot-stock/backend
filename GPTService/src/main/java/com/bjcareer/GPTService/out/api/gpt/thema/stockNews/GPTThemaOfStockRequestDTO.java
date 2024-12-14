package com.bjcareer.GPTService.out.api.gpt.thema.stockNews;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class GPTThemaOfStockRequestDTO {
	private String model;
	private List<Message> messages;
	@JsonProperty("response_format")
	private GPTResponseStockNewsOfThemaFormatDTO responseFormat;

	public GPTThemaOfStockRequestDTO(String model, List<Message> messages,
		GPTResponseStockNewsOfThemaFormatDTO responseFormat) {
		this.model = model;
		this.messages = messages;
		this.responseFormat = responseFormat;
	}

	@Getter
	public static class Message {
		private String role;
		private String content;

		public Message(String role, String content) {
			this.role = role;
			this.content = content;
		}
	}
}
