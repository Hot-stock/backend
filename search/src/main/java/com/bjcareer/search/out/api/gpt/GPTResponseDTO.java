package com.bjcareer.search.out.api.gpt;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GPTResponseDTO {
	private String id;
	private String object;
	private long created;
	private String model;
	private Usage usage;
	private Choice[] choices;
	private String systemFingerprint;

	@Data
	public static class Choice {
		private int index;
		private Message message;
		private String finishReason;
	}

	@Data
	public static class Message {
		private String role;
		private String content;  // content는 문자열로 파싱된 JSON입니다.
		private String refusal;
	}

	@Data
	public static class Usage {
		private int promptTokens;
		private int completionTokens;
		private int totalTokens;
		private PromptTokensDetails promptTokensDetails;
		private CompletionTokensDetails completionTokensDetails;
	}

	@Data
	public static class PromptTokensDetails {
		private int cachedTokens;
	}

	@Data
	public static class CompletionTokensDetails {
		private int reasoningTokens;
	}

	@Data
	public static class Content {
		private String name;
		private String reason;
		private String thema;
		private LocalDate next;
	}
}
