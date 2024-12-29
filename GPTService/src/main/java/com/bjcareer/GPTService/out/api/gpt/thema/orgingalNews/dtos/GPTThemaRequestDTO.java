package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class GPTThemaRequestDTO {
	private String model;
	private List<Message> messages;
	@JsonProperty("response_format")
	private GPTResponseThemaFormatDTO responseFormat;

	public GPTThemaRequestDTO(String model, List<Message> messages, GPTResponseThemaFormatDTO responseFormat) {
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
