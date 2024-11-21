package com.bjcareer.search.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.crawling.naver.CrawlingNaverFinance;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleCrawlingService {
	private final MeterRegistry meterRegistry;
	private final StockRepositoryPort stockRepositoryPort;
	private final ThemaInfoRepositoryPort themaInfoRepositoryPort;
	private final ThemaRepositoryPort themaRepositoryPort;
	private final LoadStockInformationPort apiServerPort;

	// @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	@Transactional
	public void run() {
		log.debug("CrawlingService started");

		Timer timer = Timer.builder("crawling.thema")
			.description("Crawling thema execution time")
			.register(meterRegistry);

		timer.record(this::executeCrawlingTask);

		log.debug("CrawlingService finished");
	}

	// Main crawling task executor
	private void executeCrawlingTask() {
		int limit = 8;

		log.debug("종목 업데이트 시작");
		Map<String, Stock> stocks = loadEntities(stockRepositoryPort.findAll(), Stock::getCode);

		List<Stock> stockInfo = apiServerPort.loadStockInfo(Market.KOSDAQ);
		stockInfo.addAll(apiServerPort.loadStockInfo(Market.KOSPI));

		stockInfo.forEach(stock -> {
			Stock test = stocks.getOrDefault(stock.getCode(), stock);
			test.updateStockInfo(stock);
			stocks.putIfAbsent(test.getCode(), test);
		});

		stockRepositoryPort.saveALl(stocks.values());
		log.debug("종목 업데이트 끝");


		Map<String, ThemaInfo> themaInfos = loadEntities(themaInfoRepositoryPort.findAll(), ThemaInfo::getName);
		Map<String, Thema> themas = loadEntities(themaRepositoryPort.findAll(), Thema::getKey);

		CrawlingNaverFinance crawlingNaverFinance = new CrawlingNaverFinance(stocks, themaInfos, themas);

		// Execute tasks concurrently using virtual threads
		runConcurrentCrawling(crawlingNaverFinance, limit);

		saveAllEntities(themaInfos, themas);
	}

	// Run crawling tasks concurrently using virtual threads
	private void runConcurrentCrawling(CrawlingNaverFinance crawlingNaverFinance, int limit) {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);


			for (int i = 1; i <= limit; i++) {
				int finalI = i;
				completionService.submit(() -> {
					submitCrawlingTask(executor, crawlingNaverFinance, finalI);
					return null;
				});
			}

			for (int i = 1; i <= limit; i++) {
				completionService.take().get();
				log.debug("Crawling task completed: {}", i);
			}

		} catch (Exception e) {
			log.error("Error while executing concurrent crawling tasks", e);
		}
	}

	// Submit individual crawling task
	private void submitCrawlingTask(ExecutorService executor, CrawlingNaverFinance crawlingNaverFinance, int page) {
		executor.submit(() -> {
			try {
				crawlingNaverFinance.crawlingThema(page);
			} catch (Exception e) {
				log.error("Error while crawling page: " + page, e);
			}
		});
	}

	// Generic method to load entities into a map
	private <T, K> Map<K, T> loadEntities(List<T> entities, Function<T, K> keyMapper) {
		return entities.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}

	// Save all entities to their respective repositories
	private void saveAllEntities(Map<String, ThemaInfo> themaInfos,
		Map<String, Thema> themas) {
		try {
			themaInfoRepositoryPort.saveAll(new ArrayList<>(themaInfos.values()));
			themaRepositoryPort.saveAll(new ArrayList<>(themas.values()));
		} catch (DataAccessException e) {
			log.error("Error while saving entities", e);
		}
	}
}
