package com.bjcareer.GPTService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.config.gpt.GPTWebConfig;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.TrainService;
import com.bjcareer.GPTService.out.api.gpt.common.variable.NextScheduleReasonResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.gpt.news.Prompt.QuestionPrompt;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TrainStockServiceTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	GPTNewsAdapter gptNewsAdapter;

	@Autowired
	private GPTStockAnalyzeService analyzeService;

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_뉴스_파일_생성() throws JsonProcessingException {
		String stockName = "제우스";
		String fileName = "./test-4o-mini" + stockName + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();

		LocalDate startDate = LocalDate.of(2024, 11, 25);
		LocalDate endDate = LocalDate.now();

		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(stockName, startDate, endDate));

		for (NewsResponseDTO newsResponseDTO : newsResponseDTOS) {
			pythonSearchServerAdapter.fetchNewsBody(
				newsResponseDTO.getLink()).ifPresent(targetNews::add);
		}

		processNews(targetNews, stockName);
		saveTrainsToFile(fileName);
	}

	private void processNews(List<OriginalNews> newsList, String stockName) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (OriginalNews news : newsList) {
			TrainService trainService = createTrainServiceWithMessages(stockName, news, mapper);
			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(String stockName, OriginalNews news,
		ObjectMapper mapper) throws JsonProcessingException {

		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_NEWS_TEXT);

		Optional<GPTNewsDomain> stockRaiseReason = gptNewsAdapter.findStockRaiseReason(news, stockName,
			news.getPubDate());

		String userPrompt = generateUserPrompt(stockName, news);
		trainService.addMessage(GPTWebConfig.USER_ROLE, userPrompt);

		if (stockRaiseReason.isEmpty()) {
			String assistantResponse = createEmptyAssistantResponse(mapper, stockName);
			trainService.addMessage("assistant", assistantResponse);
			return trainService;
		} else {
			GPTNewsDomain reason = stockRaiseReason.get();
			String assistantResponse = createAssistantResponse(mapper, stockName, reason);
			trainService.addMessage("assistant", assistantResponse);
			return trainService;
		}
	}

	private String generateUserPrompt(String stockName, OriginalNews news) {
		return QuestionPrompt.QUESTION_FORMAT.formatted(news.getPubDate(), stockName, news.getContent());
	}

	private String createEmptyAssistantResponse(ObjectMapper mapper, String stockName) throws JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(false, stockName, "", "", new NextScheduleReasonResponseDTO("", "")));
	}

	private String createAssistantResponse(ObjectMapper mapper, String stockName, GPTNewsDomain reason) throws
		JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(reason.isRelated(), stockName, reason.getReason(), reason.getNext().toString(),
				new NextScheduleReasonResponseDTO(reason.getNextReasonFact(), reason.getNextReasonFact())));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}