package com.bjcareer.GPTService.out.api.gpt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class TrainService {
	@JsonIgnore
	private static final ObjectMapper mapper = new ObjectMapper();
	private List<Message> messages = new ArrayList<>();

	public void addMessage(String role, Object content) {
		this.messages.add(new Message(role, content));
	}

	@Getter
	public static class Message {
		private String role;
		private Object content;

		public Message(String role, Object content) {
			this.role = role;
			this.content = content;
		}
	}

	// content 객체를 JSON 파일에 저장하는 함수
	public static void saveChatToFile(String path, TrainService content) {
		try (FileWriter writer = new FileWriter(path, true)) {
			String jsonContent = mapper.writeValueAsString(content);
			writer.write(jsonContent + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Getter
	public static class NewsPrompt {
		@JsonProperty("isRelevant")
		private final boolean isRelevant;
		@JsonProperty("isThema")
		private final boolean isThema;
		@JsonProperty("next_reason")
		private final NextScheduleReasonResponseDTO nextReason;

		private String isRelevantDetail;
		private final String name;
		private final String reason;
		private final String next;
		private final List<String> keywords;

		public NewsPrompt(boolean isRelevant, String isRelevantDetail, boolean isThema, List<String> keywords,
			String name, String reason,
			String next,
			NextScheduleReasonResponseDTO nextReason) {
			this.isRelevant = isRelevant;
			this.isRelevantDetail = isRelevantDetail;
			this.isThema = isThema;
			this.keywords = keywords;
			this.name = name;
			this.reason = reason;
			this.next = next;
			this.nextReason = nextReason;
		}

		@JsonProperty("isRelevant") // 명시적으로 JSON 키 설정
		public boolean isRelevant() {
			return isRelevant;
		}

		@JsonProperty("isThema") // 명시적으로 JSON 키 설정
		public boolean isThema() {
			return isThema;
		}
	}

	@Getter
	public static class GPTTrainThema {

		@JsonProperty("isPositive")
		private boolean isPositive;
		private List<ThemaInfo> thema;

		public GPTTrainThema(boolean isPositive, List<ThemaInfo> thema) {
			this.isPositive = isPositive;
			this.thema = thema;
		}

		@JsonProperty("isPositive") // 명시적으로 JSON 키 설정
		public boolean getIsPositive() {
			return isPositive;
		}
	}

}
