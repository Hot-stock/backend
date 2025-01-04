package com.bjcareer.GPTService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.bjcareer.GPTService.out.api.gpt.news.dtos.GPTNewsResponseDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TrainStockServiceTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private RedisThemaRepository redisThemaRepository;

	@Autowired
	GPTNewsAdapter gptNewsAdapter;

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_뉴스_파일_생성() throws JsonProcessingException {
		//씨큐센부터 option 없음
		String stockName = "르노코리아";//ㅁ //태영건설ㄴ
		String fileName = "./test-4o-mini" + stockName + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();


		LocalDate startDate = LocalDate.now().minusDays(5);
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
		String themas = redisThemaRepository.loadThema().stream().collect(Collectors.joining(","));
		return QuestionPrompt.QUESTION_FORMAT.formatted(news.getPubDate(), stockName, themas, news.getTitle(),
			news.getContent());
	}

	private String createEmptyAssistantResponse(ObjectMapper mapper, String stockName) throws JsonProcessingException {
		GPTNewsResponseDTO.Content response = new GPTNewsResponseDTO.Content();
		response.setRelevant(false);
		response.setIsRelevantDetail("");
		response.setThema(false);
		response.setKeywords(new ArrayList<>());

		response.setName(stockName);
		response.setReason("");
		response.setNext("");
		response.setNextReason(
			new NextScheduleReasonResponseDTO("", ""));
		response.setThemaName("");
		response.setThemaReason("");
		response.setThemStockNames(new ArrayList<>());

		return mapper.writeValueAsString(response);
	}

	private String createAssistantResponse(ObjectMapper mapper, String stockName, GPTNewsDomain reason) throws
		JsonProcessingException {

		String next = "";
		if (reason.getNext().isPresent()) {
			next = reason.getNext().get().toString();
		}

		GPTNewsResponseDTO.Content response = new GPTNewsResponseDTO.Content();
		response.setRelevant(reason.isRelated());
		response.setIsRelevantDetail(reason.getIsRelatedDetail());
		response.setThema(reason.isThema());
		response.setKeywords(reason.getKeywords());

		response.setName(stockName);
		response.setReason(reason.getReason());
		response.setNext(next);
		response.setNextReason(
			new NextScheduleReasonResponseDTO(reason.getNextReasonFact(), reason.getNextReasonOption()));
		response.setThemaName(reason.getThemaInfo().getName());
		response.setThemaReason(reason.getThemaInfo().getReason());
		response.setThemStockNames(reason.getThemaInfo().getStockName());

		return mapper.writeValueAsString(response);
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
