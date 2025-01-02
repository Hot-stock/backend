package com.bjcareer.GPTService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_뉴스_파일_생성() throws JsonProcessingException {
		//씨큐센부터 option 없음
		String stockName = "에스피시스템스";//ㅁ //태영건설ㄴ
		String fileName = "./test-4o-mini" + stockName + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();

		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now();

		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(stockName, startDate, endDate));

		Map<String, NewsResponseDTO> newsMap = new HashMap<>();

		newsResponseDTOS.stream()
			.filter(Objects::nonNull)
			.map(t -> newsMap.putIfAbsent(t.getLink(), t)).toList();

		List<NewsResponseDTO> target = new ArrayList<>(newsMap.values());

		for (NewsResponseDTO newsResponseDTO : target) {
			pythonSearchServerAdapter.fetchNewsBody(
				newsResponseDTO.getLink(), startDate).ifPresent(targetNews::add);

			if (targetNews.size() >= 10) {
				break;
			}
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
		return QuestionPrompt.QUESTION_FORMAT.formatted(news.getPubDate(), stockName, "", news.getTitle(), news.getContent());
	}

	private String createEmptyAssistantResponse(ObjectMapper mapper, String stockName) throws JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(false, "",false, new ArrayList<>(), stockName, "", "",
				new NextScheduleReasonResponseDTO("", "")));
	}

	private String createAssistantResponse(ObjectMapper mapper, String stockName, GPTNewsDomain reason) throws
		JsonProcessingException {
		String date = "";
		if(reason.getNext().isPresent()){
			date = reason.getNext().get().toString();
		}

		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(reason.isRelated(), reason.getIsRelatedDetail(), reason.isThema(), reason.getKeywords(), stockName,
				reason.getReason(),
				date,
				new NextScheduleReasonResponseDTO(reason.getNextReasonFact(), reason.getNextReasonFact())));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
