package com.bjcareer.GPTService.out.api.gpt.news;

import java.util.List;

import lombok.Getter;

@Getter
public class GPTNewsRequestDTO {
	private String model;
	private List<Message> messages;
	private GPTResponseNewsFormatDTO response_format;

	public GPTNewsRequestDTO(String model, List<Message> messages, GPTResponseNewsFormatDTO response_format) {
		this.model = model;
		this.messages = messages;
		this.response_format = response_format;
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
