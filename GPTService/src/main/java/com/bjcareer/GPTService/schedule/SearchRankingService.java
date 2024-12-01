package com.bjcareer.GPTService.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.in.dtos.RankingStocksDTO;
import com.bjcareer.GPTService.out.api.toss.SoarStockResponseDTO;
import com.bjcareer.GPTService.out.api.toss.TossServerAdapter;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchRankingService {
	private final GPTStockAnalyzeService gptStockAnalyzeService;
	private final RedisThemaRepository redisThemaRepository;
	private final TossServerAdapter tossServerAdapter;

	@Scheduled(cron = "0 */1 * * * *")
	void updateRanking() {
		String thema = redisThemaRepository.loadThema();

		if (thema.isEmpty()) {
			log.error("thema가 아직 복구되지 않았습니다.");
			return;
		}


		SoarStockResponseDTO soarStock = tossServerAdapter.getSoarStock();
		LocalDate baseAt = LocalDate.parse(soarStock.getResult().getBasedAt().split("T")[0]);
		List<String> stockNames = soarStock.getResult().getProducts().stream().map(
			SoarStockResponseDTO.Result.Product::getName).toList();

		RankingStocksDTO rankingStocksDTO = new RankingStocksDTO(stockNames, baseAt);
		gptStockAnalyzeService.analyzeRankingStock(rankingStocksDTO);
	}
}
