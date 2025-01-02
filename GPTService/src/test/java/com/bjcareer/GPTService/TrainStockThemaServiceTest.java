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
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.TrainService;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt.AnalyzeThemaQuestionPrompt;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.GPTThemaOfStockNewsAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class
TrainStockThemaServiceTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private RedisThemaRepository redisThemaRepository;

	@Autowired
	GPTThemaOfStockNewsAdapter gptThemaAdapter;

	@Autowired
	GPTNewsAdapter gptNewsAdapter;

	@Autowired
	GPTStockNewsRepository gptStockNewsRepository;

	private final List<TrainService> trains = new ArrayList<>();

	@Test
	void 테스트_테마_파일_생성() throws JsonProcessingException {
		String keyword = "태광";  //알티캐스트, 액션스퀘어
		String fileName = "./thema-" + keyword + ".json";
		List<OriginalNews> targetNews = new ArrayList<>();
		List<GPTNewsDomain> targetGPTNews = new ArrayList<>();

		LocalDate startDate = LocalDate.of(2024, 12, 19);
		LocalDate endDate = LocalDate.of(2024, 12, 19);

		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, startDate, endDate));

		for (NewsResponseDTO newsResponseDTO : newsResponseDTOS) {
			pythonSearchServerAdapter.fetchNewsBody(
				newsResponseDTO.getLink(), startDate).ifPresent(targetNews::add);
		}

		for (OriginalNews news : targetNews) {
			if (targetGPTNews.size() >= 10) {
				break;
			}

			Optional<GPTNewsDomain> byLink = gptStockNewsRepository.findByLink(news.getNewsLink());

			if (byLink.isPresent()) {
				GPTNewsDomain gptNewsDomain = byLink.get();
				if (gptNewsDomain.isRelated() && gptNewsDomain.isThema()) {
					targetGPTNews.add(gptNewsDomain);
				}
				continue;
			}

			gptNewsAdapter.findStockRaiseReason(news, keyword, news.getPubDate())
				.filter(t -> t.isRelated() && t.isThema())
				.ifPresent(targetGPTNews::add);
		}

		processNews(targetGPTNews, "");
		saveTrainsToFile(fileName);
	}

	private void processNews(List<GPTNewsDomain> newsList, String knownThema) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (GPTNewsDomain news : newsList) {
			TrainService trainService = createTrainServiceWithMessages(knownThema, news, mapper);

			if (trainService == null) {
				continue;
			}

			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(String knownThema, GPTNewsDomain news,
		ObjectMapper mapper) throws JsonProcessingException {

		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_THEMA_TEXT);

		Optional<GPTStockThema> optGptThema = gptThemaAdapter.getThema(news);

		String userPrompt = generateUserPrompt(news.getNews(), news.getStockName(), knownThema);
		trainService.addMessage(GPTWebConfig.USER_ROLE, userPrompt);

		if (optGptThema.isEmpty()) {
			return null;
		} else {
			GPTStockThema gptStockThema = optGptThema.get();
			String assistantResponse = createAssistantResponse(mapper, gptStockThema);
			trainService.addMessage("assistant", assistantResponse);
			return trainService;
		}
	}

	private String generateUserPrompt(OriginalNews news, String stockName, String knownThema) {
		return AnalyzeThemaQuestionPrompt.QUESTION_PROMPT.formatted(news.getPubDate(), news.getContent(), stockName, knownThema);
	}

	private String createAssistantResponse(ObjectMapper mapper, GPTStockThema stockThema) throws
		JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.GPTTrainThema(stockThema.isPositive(), stockThema.getThemaInfo()));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
