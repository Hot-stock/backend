package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import java.util.List;

import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTResponseThemaFormatDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class GPTTriggerRequestDTO {
	private String model;
	private List<Message> messages;
	@JsonProperty("response_format")
	private GPTTriggerFormatResponseDTO responseFormat;

	public GPTTriggerRequestDTO(String model, List<Message> messages, GPTTriggerFormatResponseDTO responseFormat) {
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
