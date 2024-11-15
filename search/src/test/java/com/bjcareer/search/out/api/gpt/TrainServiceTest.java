package com.bjcareer.search.out.api.gpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;
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

	private final List<TrainService> trains = new ArrayList<>();
	private final Random random = new Random();

	@Test
	@Transactional
	void 테스트_파일_생성() throws JsonProcessingException {
		List<Stock> allStocks = stockRepositoryPort.findAll();
		String stockNames = getStockNames(allStocks);

		String stockName = "진양산업";
		String fileName = "./test" + stockName + ".json";
		int threshold = 10;
		int numberOfRepetitions = 5;

		Stock stock = stockRepositoryPort.findByName(stockName).orElseThrow(() ->
			new IllegalArgumentException("Stock not found: " + stockName));

		StockChart ohlcAboveThreshold = getStockChartAboveThreshold(stock, threshold);

		for (int i = 0; i < ohlcAboveThreshold.getOhlcList().size(); i++) {
			List<News> newsList = fetchNews(stock, ohlcAboveThreshold, i);

			String themas = getThemasAsString(stock);

			processNews(newsList, stock, stockNames, themas);

			if (i == numberOfRepetitions) {
				break; // 조건에 따라 루프 중단
			}
		}

		saveTrainsToFile(fileName);
	}

	private String getStockNames(List<Stock> stocks) {
		return stocks.stream()
			.map(Stock::getName)
			.collect(Collectors.joining(" "));
	}

	private StockChart getStockChartAboveThreshold(Stock stock, int threshold) {
		LoadChartAboveThresholdCommand command = new LoadChartAboveThresholdCommand(stock.getCode(), threshold);
		return stockChartRepositoryPort.findOhlcAboveThreshold(command);
	}

	private Stock getRandomStock(List<Stock> stocks) {
		int randomIndex = random.nextInt(stocks.size());
		return stocks.get(randomIndex);
	}

	private List<News> fetchNews(Stock stock, StockChart ohlcAboveThreshold, int index) {
		return pythonSearchServerAdapter.fetchNews(
			new NewsCommand(stock.getName(),
				ohlcAboveThreshold.getOhlcList().get(index).getDate(),
				ohlcAboveThreshold.getOhlcList().get(index).getDate()));
	}

	private String getThemasAsString(Stock stock) {
		return stock.getThemas().stream()
			.map(thema -> thema.getThemaInfo().getName())
			.collect(Collectors.joining(" "));
	}

	private void processNews(List<News> newsList, Stock stock, String stockNames, String themas) throws
		JsonProcessingException {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		for (News news : newsList) {
			TrainService trainService = createTrainServiceWithMessages(stock, news, stockNames, themas, mapper);
			trains.add(trainService);
		}
	}

	private TrainService createTrainServiceWithMessages(Stock stock, News news, String stockNames, String themas,
		ObjectMapper mapper) throws JsonProcessingException {
		TrainService trainService = new TrainService();
		trainService.addMessage(GPTWebConfig.SYSTEM_ROLE, GPTWebConfig.SYSTEM_NEWS_TEXT);

		Optional<GTPNewsDomain> stockRaiseReason = gptNewsPort.findStockRaiseReason(
			news.getContent(), stock.getName(), stockNames, themas, news.getPubDate());

		String userPrompt = generateUserPrompt(stock, news, stockNames, themas);
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

	private String generateUserPrompt(Stock stock, News news, String stockNames, String themas) {
		return String.format(" Today’s date is the news publication date: %s\n" +
				"Stock name is <stockname>%s</stockname>\n" +
				"Analyze the following news <article>%s</article>\n" +
				"Here is the Thema's name <themaName>%s</themaName>\n" +
				"Provide the response in Korean.",
			news.getPubDate(), stock.getName(), news.getContent(), stockNames, themas);
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

	private void saveTrainsToFile(String filePath) {
		trains.forEach(train -> TrainService.saveChatToFile(filePath, train));
	}
}
