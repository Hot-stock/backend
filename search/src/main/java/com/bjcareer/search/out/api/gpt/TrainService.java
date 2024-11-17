package com.bjcareer.search.out.api.gpt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.out.api.gpt.news.GPTResponseNewsFormatDTO;
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
	@JsonProperty("response_format")
	private GPTResponseNewsFormatDTO responseFormat;

	public TrainService(GPTResponseNewsFormatDTO responseFormat) {
		this.responseFormat = responseFormat;
	}

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

	@Data
	public static class NewsPrompt {
		private final boolean filtered;
		private final String name;
		private final String reason;
		private final List<GTPNewsDomain.GPTThema> themas;
		private final String next;
		private final String next_reason;

		public NewsPrompt(boolean filtered, String name, String reason,
			List<GTPNewsDomain.GPTThema> themas, String next, String next_reason) {
			this.filtered = filtered;
			this.name = name;
			this.reason = reason;
			this.themas = themas;
			this.next = next;
			this.next_reason = next_reason;
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
}
