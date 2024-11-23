package com.bjcareer.GPTService.out.api.gpt.insight;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;

@Getter
public class GPTRequestInsightDTO {
	private final String model;
	private final List<Message> messages;
	@JsonAlias("response_format")
	private final GPTResponseInsightFormatDTO response_format;

	public GPTRequestInsightDTO(String model, List<Message> messages, GPTResponseInsightFormatDTO responseFormat) {
		this.model = model;
		this.messages = messages;
		this.response_format = responseFormat;
	}

	@Getter
	public static class Message {
		private final String role;
		private final String content;

		public Message(String role, String content) {
			this.role = role;
			this.content = content;
		}
	}

}
