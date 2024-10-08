package com.bjcareer.search.out.api.gpt;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.GPTWebTestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;

class ChatGPTTest {

	@Test
	void test() throws JsonProcessingException {
		WebClient.Builder builder = WebClient.builder();
		GPTWebTestConfig config = new GPTWebTestConfig();
		WebClient webClient = config.webClient(builder);

		ChatGPT chatGPT = new ChatGPT(webClient);
		chatGPT.getStockReason("https://www.newsway.co.kr/news/view?ud=2024100811542097961");

		// GPTResponseDTO gptResponseDTO = MockData.getGPTResponseDTO();
		// System.out.println("gptResponseDTO = " + gptResponseDTO);
	}

}
