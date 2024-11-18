package com.bjcareer.search.out.api.gpt.news;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // 불필요한 필드 무시
public class GPTNewsResponseDTO {
	private List<Choice> choices = new ArrayList<>();

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)  // 불필요한 필드 무시
	public static class Choice {
		private Message message;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)  // 불필요한 필드 무시
	public static class Message {
		private String content;  // content는 문자열로 파싱된 JSON입니다.

		public Content getParsedContent() {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(content, Content.class);  // content를 Content 객체로 변환
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Data
	public static class Content {
		@JsonProperty("isFakeNews")  // JSON 필드 "isFiltered"를 매핑
		private boolean isFakeNews;
		private String name;
		private String reason;
		private List<ThemaVariableResponseDTO> themas;
		private String next;
		@JsonProperty("next_reason")  // JSON의 next_reason 필드를 nextReason 필드에 매핑
		private String nextReason;
	}
}
