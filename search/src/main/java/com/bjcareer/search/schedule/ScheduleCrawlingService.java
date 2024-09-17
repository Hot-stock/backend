package com.bjcareer.search.schedule;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.crawling.naver.CrawlingThema;
import com.bjcareer.search.repository.stock.StockRepository;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.repository.stock.ThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduleCrawlingService {
	public final Integer MAX_PAGE = 8;

	private final ThemaInfoRepository themaInfoRepository;
	private final StockRepository stockRepository;
	private final ThemaRepository themaRepository;

	@Scheduled(cron = "0 30 4 * * *")
	@Transactional
	public void run() throws Exception {
		long startTime = System.currentTimeMillis(); // 시작 시간 측정
		log.info("CrawlingService run");

		Map<String, Stock> stocks = stockRepository.findAll()
			.stream()
			.collect(Collectors.toMap(Stock::getCode, stock -> stock));

		Map<String, ThemaInfo> themaInfos = themaInfoRepository.findAll()
			.stream()
			.collect(Collectors.toMap(ThemaInfo::getName, themaInfo -> themaInfo));

		Map<String, Thema> themas = themaRepository.findAll()
			.stream()
			.collect(Collectors.toMap(Thema::getKey, thema -> thema));


		CrawlingThema crawlingThema = new CrawlingThema(stocks, themaInfos, themas);


		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) { // 경량 쓰레드 전용 Executor 사용
			for (int i = 1; i <= MAX_PAGE; i++) {
				int page = i; // 람다식에서 변수 참조를 위해 final처럼 사용
				executor.submit(() -> {
					try {
						crawlingThema.crawlingThema(page); // 크롤링 작업을 경량 쓰레드에서 실행
					} catch (Exception e) {
						log.error("Error while crawling page: " + page, e);
					}
				});
			}
		}

		long endTime = System.currentTimeMillis(); // 종료 시간 측정
		long duration = endTime - startTime; // 수행 시간 계산
		log.info("CrawlingService finished in " + duration + " ms");
		stockRepository.saveAll(new ArrayList<>(stocks.values()));
		themaInfoRepository.saveAll(new ArrayList<>(themaInfos.values()));
		themaRepository.saveAll(new ArrayList<>(themas.values()));
		log.info("CrawlingService saved themas");
	}
}
