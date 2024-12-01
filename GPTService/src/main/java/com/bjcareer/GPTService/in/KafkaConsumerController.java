package com.bjcareer.GPTService.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.AnalyzeBestNews;
import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.application.port.in.AnalyzeStockNewsCommand;
import com.bjcareer.GPTService.in.dtos.RankingStocksDTO;
import com.bjcareer.GPTService.out.persistence.redis.RedisMarketRankAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerController {
	private final GPTStockAnalyzeService analyzeService;
	private final AnalyzeBestNews analyzeBestNews;
	private final RedisMarketRankAdapter redisMarketRankAdapter;


	@KafkaListener(topics = "analyze-news-stock-topic", groupId = "analyze-news-stock-group")
	public void consumeAnalyzeStock(AnalyzeStockNewsCommand command, Acknowledgment acknowledgment) {
		try {
			analyzeService.analyzeStockNewsByNewsLink(command);
		} catch (Exception e) {
			log.debug("Can't save Error: {} {}", e, command.getNewsLink());
		} finally {
			acknowledgment.acknowledge(); // 메시지 처리 완료 후 커밋
		}
	}

	@KafkaListener(topics = "analyze-ranking-stock", groupId = "analyze-hot-ranking-group")
	public void consumeAnalyzeRankingStock(RankingStocksDTO command, Acknowledgment acknowledgment) {
		try {
			analyzeService.analyzeRankingStock(command);
		} catch (Exception e) {
			log.error("Rank Error: {} {}", e, command);
		} finally {
			acknowledgment.acknowledge(); // 메시지 처리 완료 후 커밋
		}
	}
}
