package com.bjcareer.search.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
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

	@Scheduled(cron = "35 3 * * * *")
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

		Map<String, Stock> stocks = loadEntities(stockRepositoryPort.findAll(), Stock::getCode);
		Map<String, ThemaInfo> themaInfos = loadEntities(themaInfoRepositoryPort.findAll(), ThemaInfo::getName);
		Map<String, Thema> themas = loadEntities(themaRepositoryPort.findAll(), Thema::getKey);

		CrawlingNaverFinance crawlingNaverFinance = new CrawlingNaverFinance(stocks, themaInfos, themas);

		// Execute tasks concurrently using virtual threads
		runConcurrentCrawling(crawlingNaverFinance, limit);

		// Save the results
		saveAllEntities(stocks, themaInfos, themas);
	}

	// Run crawling tasks concurrently using virtual threads
	private void runConcurrentCrawling(CrawlingNaverFinance crawlingNaverFinance, int limit) {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 1; i <= limit; i++) {
				submitCrawlingTask(executor, crawlingNaverFinance, i);
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
	private void saveAllEntities(Map<String, Stock> stocks, Map<String, ThemaInfo> themaInfos,
		Map<String, Thema> themas) {
		try {
			log.info("Entities saved successfully");
			themaInfoRepositoryPort.saveAll(new ArrayList<>(themaInfos.values()));
			themaRepositoryPort.saveAll(new ArrayList<>(themas.values()));
		} catch (DataAccessException e) {
			log.error("Error while saving entities", e);
		}
	}
}
