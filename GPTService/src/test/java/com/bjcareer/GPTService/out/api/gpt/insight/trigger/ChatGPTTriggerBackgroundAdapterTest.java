package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.AnalyzeRaiseBackground;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;

@SpringBootTest
class ChatGPTTriggerBackgroundAdapterTest {
	@Autowired
	private ChatGPTTriggerAdapter chatGPTTriggerAdapter;
	@Autowired
	private AnalyzeRaiseBackground analyzeRaiseBackground;

	@Test
	void test() {
		String thema = "우크라이나 재건";
		Optional<GPTTriggerBackground> trigger1 = analyzeRaiseBackground.loadTriggerOfRiseOfThema(thema);

		// Optional<GPTTriggerBackground> trigger = chatGPTTriggerAdapter.getTrigger(reasons, thema,
		// 	ChatGPTTriggerAdapter.GPT_4o);
	}
}
