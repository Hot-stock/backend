package com.bjcareer.GPTService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.TrainService;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.QuestionPrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.GPTThemaAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TrainThemaServiceTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	GPTThemaAdapter gptThemaAdapter;

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_뉴스_파일_생성() throws JsonProcessingException {
		String keyword = "진성티이씨";
		String fileName = "./test-4o-mini" + keyword + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();

		LocalDate startDate = LocalDate.of(2024, 11, 26);
		LocalDate endDate = LocalDate.now();

		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, startDate, endDate));

		for (NewsResponseDTO newsResponseDTO : newsResponseDTOS) {
			pythonSearchServerAdapter.fetchNewsBody(
				newsResponseDTO.getLink()).ifPresent(targetNews::add);
		}

		processNews(targetNews, "우크라이나 재건, 인프라 투자");
		saveTrainsToFile(fileName);
	}

	private void processNews(List<OriginalNews> newsList, String knownThema) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (OriginalNews news : newsList) {
			TrainService trainService = createTrainServiceWithMessages(knownThema, news, mapper);

			if (trainService == null) {
				continue;
			}

			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(String knownThema, OriginalNews news,
		ObjectMapper mapper) throws JsonProcessingException {

		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_THEMA_TEXT);

		Optional<GPTThema> optGptThema = gptThemaAdapter.summaryThemaNews(news, knownThema);

		String userPrompt = generateUserPrompt(knownThema, news);
		trainService.addMessage(GPTWebConfig.USER_ROLE, userPrompt);

		if (optGptThema.isEmpty()) {
			return null;
		} else {
			GPTThema gptThema = optGptThema.get();
			String assistantResponse = createAssistantResponse(mapper, gptThema);
			trainService.addMessage("assistant", assistantResponse);
			return trainService;
		}
	}

	private String generateUserPrompt(String stockName, OriginalNews news) {
		return QuestionPrompt.QUESTION_FORMAT.formatted(news.getPubDate(), stockName, news.getContent());
	}

	private String createAssistantResponse(ObjectMapper mapper, GPTThema reason) throws
		JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.GPTTrainThema(reason.isPositive(), reason.getSummary(), reason.getUpcomingDate(),
				new NextScheduleReasonResponseDTO(reason.getUpcomingDateReasonFact(),
					reason.getUpcomingDateReasonOpinion()), reason.getThemaInfo(), reason.isRelatedThema()));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
