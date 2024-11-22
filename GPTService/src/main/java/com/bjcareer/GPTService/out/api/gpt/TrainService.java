package com.bjcareer.GPTService.out.api.gpt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
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
		@JsonProperty("isFakeNews")
		private final boolean isFakeNews;

		private final String name;
		private final String reason;
		private final List<GPTNewsDomain.GPTThema> themas;
		private final String next;
		private final String next_reason;

		public NewsPrompt(boolean isFakeNews, String name, String reason,
			List<GPTNewsDomain.GPTThema> themas, String next, String next_reason) {
			this.isFakeNews = isFakeNews;
			this.name = name;
			this.reason = reason;
			this.themas = themas;
			this.next = next;
			this.next_reason = next_reason;
		}

		@JsonProperty("isFakeNews") // 명시적으로 JSON 키 설정
		public boolean isFakeNews() {
			return isFakeNews;
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
	public static class GPTThema {
		@JsonProperty("isRelatedThema")
		private final boolean isRelatedThema;
		private final String summary;

		private final String upcomingDate;
		private final String upcomingDateReason;

		public GPTThema(boolean isRelatedThema, String summary, String upcomingDate, String upcomingDateReason) {
			this.isRelatedThema = isRelatedThema;
			this.summary = summary;
			this.upcomingDate = upcomingDate;
			this.upcomingDateReason = upcomingDateReason;
		}

		@JsonProperty("isRelatedThema") // 명시적으로 JSON 키 설정
		public boolean getIsRelatedThema() {
			return isRelatedThema;
		}
	}

}
