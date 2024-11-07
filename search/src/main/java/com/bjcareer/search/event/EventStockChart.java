package com.bjcareer.search.event;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.StockChart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStockChart {
	private final Queue<StockChartQueryCommand> eventQueue = new LinkedList<>();
	private final LoadStockInformationPort usecase;
	private final StockChartRepositoryPort stockChartRepositoryPort;

	@EventListener
	public void handle(StockChartQueryCommand command) {
		log.debug("Update stockChart {}", command.getStock());
		eventQueue.add(command);
	}

	@Scheduled(fixedDelay = 1000)  // 5초마다 실행
	public void processQueue() {
		while (!eventQueue.isEmpty()) {
			StockChartQueryCommand poll = eventQueue.poll();
			StockChart chart = usecase.loadStockChart(poll);

			stockChartRepositoryPort.save(chart);
			log.debug("StockChart updated: {}", chart);
		}
	}
}
