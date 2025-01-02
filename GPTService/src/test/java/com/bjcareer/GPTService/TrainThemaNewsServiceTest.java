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
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTThemaAdapter;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.prompt.ThemaQuestionPrompt;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class
TrainThemaNewsServiceTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private RedisThemaRepository redisThemaRepository;

	@Autowired
	GPTThemaAdapter gptThemaAdapter;

	@Autowired
	GPTStockNewsRepository gptStockNewsRepository;

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_테마_파일_생성() throws JsonProcessingException {
		String keyword = "이재명";
		String fileName = "./thema-" + keyword + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();

		LocalDate startDate = LocalDate.of(2024, 1, 26);
		LocalDate endDate = LocalDate.of(2024, 12, 26);

		// redisThemaRepository.loadThema();

		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, startDate, endDate));

		for (NewsResponseDTO newsResponseDTO : newsResponseDTOS) {
			pythonSearchServerAdapter.fetchNewsBody(
				newsResponseDTO.getLink(), startDate).ifPresent(targetNews::add);

			if (targetNews.size() >= 10) {
				break;
			}
		}

		processNews(targetNews, keyword);
		saveTrainsToFile(fileName);
	}

	private void processNews(List<OriginalNews> target, String thema) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (OriginalNews news : target) {
			TrainService trainService = createTrainServiceWithMessages(thema, news, mapper);

			if (trainService == null) {
				continue;
			}

			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(String knownThema, OriginalNews news,
		ObjectMapper mapper) throws JsonProcessingException {

		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_NEWS_TEXT);

		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(news, knownThema, GPTThemaAdapter.GPT_4o);

		String userPrompt = generateUserPrompt(news, knownThema);
		trainService.addMessage(GPTWebConfig.USER_ROLE, userPrompt);

		if (gptThema.isEmpty()) {
			return null;
		} else {
			GPTThema thema = gptThema.get();
			String assistantResponse = createAssistantResponse(mapper, thema);
			trainService.addMessage("assistant", assistantResponse);
			return trainService;
		}
	}

	private String generateUserPrompt(OriginalNews news, String thema) {
		return ThemaQuestionPrompt.QUESTION_PROMPT.formatted(news.getPubDate(), news.getContent(), thema);
	}

	private String createAssistantResponse(ObjectMapper mapper, GPTThema thema) throws
		JsonProcessingException {
		boolean empty = thema.getUpcomingDate().isEmpty();
		String upcomingDate = "";
		if (!empty) {
			upcomingDate = thema.getUpcomingDate().get().toString();
		}
		return mapper.writeValueAsString(
			new TrainService.GPTTrainThemaOriginalNews(thema.isRelatedThema(), thema.getThemaInfo().getReason(),
				thema.getSummary(),
				upcomingDate, new NextScheduleReasonResponseDTO(thema.getUpcomingDateReasonFact(),
				thema.getUpcomingDateReasonOpinion()), thema.getStockName()));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
