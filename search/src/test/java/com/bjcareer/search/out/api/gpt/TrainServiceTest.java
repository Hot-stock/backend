package com.bjcareer.search.out.api.gpt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.GPTNewsPort;
import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.config.gpt.GPTWebConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.bjcareer.search.out.api.gpt.news.QuestionPrompt;
import com.bjcareer.search.out.api.gpt.thema.ChatGPTThemaAdapter;
import com.bjcareer.search.out.api.gpt.thema.ThemaQuestionPrompt;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.search.out.persistence.thema.ThemaInfoRepositoryAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class TrainServiceTest {

	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private StockChartRepositoryPort stockChartRepositoryPort;

	@Autowired
	private StockRepositoryPort stockRepositoryPort;

	@Autowired
	private GPTNewsPort gptNewsPort;

	@Autowired
	private ChatGPTThemaAdapter chatGPTThemaAdapter;

	private final List<TrainService> trains = new ArrayList<>();
	@Autowired
	private ThemaInfoRepositoryAdapter themaInfoRepositoryAdapter;

	@Test
	@Transactional
	void 테스트_뉴스_파일_생성() throws JsonProcessingException {
		String stockName = "팜스코";
		String fileName = "./test-4o-mini" + stockName + ".json";
		int threshold = 10;
		int numberOfRepetitions = 0;

		Stock stock = stockRepositoryPort.findByName(stockName).orElseThrow(() ->
			new IllegalArgumentException("Stock not found: " + stockName));

		String themas = themaInfoRepositoryAdapter.findAll()
			.stream()
			.map(ThemaInfo::getName)
			.collect(Collectors.joining());

		StockChart ohlcAboveThreshold = getStockChartAboveThreshold(stock, threshold);

		for (int i = 0; i < ohlcAboveThreshold.getOhlcList().size(); i++) {
			List<News> newsList = fetchNews(
				new NewsCommand(stock.getName(), ohlcAboveThreshold.getOhlcList().get(i).getDate(),
					ohlcAboveThreshold.getOhlcList().get(i).getDate()));

			processNews(newsList, stock, themas);

			if (i == numberOfRepetitions) {
				break; // 조건에 따라 루프 중단
			}
		}

		saveTrainsToFile(fileName);
	}

	@Test
	void 테스트_테마_파일_생성() throws JsonProcessingException {
		String thema = "한한령";
		String fileName = "./test-4o-re" + thema + ".json";

		LocalDate startDate = LocalDate.of(2024, 11, 19);
		LocalDate endDate = LocalDate.of(2024, 11, 19);

		NewsCommand command = new NewsCommand(thema, startDate, endDate);
		List<News> newsList = fetchNews(command);

		for (News news : newsList) {
			TrainService trainService = new TrainService();
			trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_THEMA_TEXT);
			trainService.addMessage(GPTWebConfig.USER_ROLE,
				ThemaQuestionPrompt.QUESTION_PROMPT.formatted(news.getPubDate(), thema, news.getContent()));

			Optional<GPTThema> gptThema = chatGPTThemaAdapter.summaryThemaNews(news, thema);

			if (gptThema.isPresent()) {
				System.out.println("news.getOriginalLink() = " + news.getOriginalLink());
				String assistantResponse = createThemaAssistantResponse(AppConfig.customObjectMapper(),
					gptThema.get().isRelatedThema(),
					gptThema.get());
				trainService.addMessage("assistant", assistantResponse);
				trains.add(trainService);
			}
		}

		saveTrainsToFile(fileName);
	}



	private StockChart getStockChartAboveThreshold(Stock stock, int threshold) {
		LoadChartAboveThresholdCommand command = new LoadChartAboveThresholdCommand(stock.getCode(), threshold);
		return stockChartRepositoryPort.findOhlcAboveThreshold(command);
	}

	private List<News> fetchNews(NewsCommand newsCommand) {
		return pythonSearchServerAdapter.fetchNews(newsCommand);
	}

	private void processNews(List<News> newsList, Stock stock, String themas) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (News news : newsList) {
			System.out.println("news.getOriginalLink() = " + news.getOriginalLink());
			TrainService trainService = createTrainServiceWithMessages(stock, news, themas, mapper);
			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(Stock stock, News news, String themas,
		ObjectMapper mapper) throws JsonProcessingException {

		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_NEWS_TEXT);

		Optional<GTPNewsDomain> stockRaiseReason = gptNewsPort.findStockRaiseReason(
			news.getContent(), stock.getName(), news.getPubDate());

		String userPrompt = generateUserPrompt(stock, news);
		trainService.addMessage(GPTWebConfig.USER_ROLE, userPrompt);

		String assistantResponse = stockRaiseReason.map(reason -> {
				try {
					return createAssistantResponse(mapper, stock, reason);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			})
			.orElse(createEmptyAssistantResponse(mapper, stock));

		trainService.addMessage("assistant", assistantResponse);
		return trainService;
	}

	private String generateUserPrompt(Stock stock, News news) {
		return QuestionPrompt.QUESTION_FORMAT.formatted(news.getPubDate(), stock.getName(), news.getContent(),
			stock.getName());
	}

	private String createEmptyAssistantResponse(ObjectMapper mapper, Stock stock) throws JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(true, stock.getName(), "", new ArrayList<>(), "", ""));
	}

	private String createAssistantResponse(ObjectMapper mapper, Stock stock, GTPNewsDomain reason) throws
		JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.NewsPrompt(false, stock.getName(), reason.getReason(),
				reason.getThemas(), reason.getNext().toString(), reason.getNextReason()));
	}

	private String createThemaAssistantResponse(ObjectMapper mapper, boolean isRelated, GPTThema thema) throws
		JsonProcessingException {
		return mapper.writeValueAsString(
			new TrainService.GPTThema(isRelated, thema.getSummary(), thema.getUpcomingDate(),
				thema.getUpcomingDateReason()));
	}

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
