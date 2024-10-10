package com.bjcareer.search.out.api.gpt;

import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
public class GPTRequestDTO {
	private String model;
	private List<Message> messages;
	private GPTResponseFormatDTO response_format;

	public GPTRequestDTO(String model, List<Message> messages, GPTResponseFormatDTO response_format) {
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
