package com.bjcareer.GPTService.out.api.gpt;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

		private final String name;
		private final String reason;
		private final String next;
		@JsonProperty("next_reason")
		private final NextScheduleReasonResponseDTO nextReason;

		public NewsPrompt(boolean isRelevant, String name, String reason, String next,
			NextScheduleReasonResponseDTO nextReason) {
			this.isRelevant = isRelevant;
			this.name = name;
			this.reason = reason;
			this.next = next;
			this.nextReason = nextReason;
		}

		@JsonProperty("isRelevant") // 명시적으로 JSON 키 설정
		public boolean isRelevant() {
			return isRelevant;
		}

		@Data
		@NoArgsConstructor
		public static class ThemaPrompt {
			private String name;
			private String reason;

			public ThemaPrompt(String name, String reason) {
				this.name = name;
				this.reason = reason;
			}
		}
	}

	@Getter
	public static class GPTTrainThema {
		@JsonProperty("isRealNew")
		private final boolean isRealNew;
		@JsonProperty("isPositive")
		private final boolean isPositive;
		private final String summary;
		private final String upcomingDate;
		private final NextScheduleReasonResponseDTO upcomingDateReason;
		private final ThemaInfo thema;

		public GPTTrainThema(boolean isPositive, String summary, LocalDate upcomingDate,
			NextScheduleReasonResponseDTO upcomingDateReason, ThemaInfo thema, boolean isRealNew) {
			this.isPositive = isPositive;
			this.summary = summary;

			if (upcomingDate != null) {
				this.upcomingDate = upcomingDate.toString();
			} else {
				this.upcomingDate = "";
			}
			this.upcomingDateReason = upcomingDateReason;
			this.thema = thema;
			this.isRealNew = isRealNew;
		}

		@JsonProperty("isRealNew") // 명시적으로 JSON 키 설정
		public boolean getIsRealNew() {
			return isRealNew;
		}

		@JsonProperty("isPositive") // 명시적으로 JSON 키 설정
		public boolean getIsPositive() {
			return isPositive;
		}
	}

}
