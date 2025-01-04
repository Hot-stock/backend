package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.GPTService.application.aop.AnalyzeBackground;
import com.bjcareer.GPTService.application.port.in.AnalyzeStockNewsCommand;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.domain.Stock;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.in.dtos.RankingStocksDTO;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.python.ParseNewsContentResponseDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.rdb.StockRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisMarketRankAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTStockAnalyzeService {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTNewsAdapter gptNewsAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final RedisMarketRankAdapter redisMarketRankAdapter;
	private final StockRepository stockRepository;

	/**
	 * Handles asynchronous requests, typically triggered via Kafka.
	 */

	@AnalyzeBackground
	public GPTNewsDomain analyzeStockNewsByNewsLink(AnalyzeStockNewsCommand command) {
		// if (isNewsNotProcessed(command.getNewsLink())) {
		// 	Optional<GPTNewsDomain> optionalGPTNewsDomain = processAnalyzeNewsLink(command.getNewsLink(), LocalDate.now());
		// 	optionalGPTNewsDomain.ifPresent(gptStockNewsRepository::save);
		// }

		return gptStockNewsRepository.findByLink(command.getNewsLink()).orElseThrow();
	}

	@AnalyzeBackground
	@Transactional(readOnly = true)
	public List<GPTNewsDomain> analyzeStockNewsByDateWithStockName(LocalDate endDate, String stockName) {
		Map<String, String> stockMap = getStockDomainToHash();
		log.debug("Analyzing stock news for date: {} stock: {}", endDate, stockName);
		List<NewsResponseDTO> newsLinks = fetchNewsForStock(endDate, stockName);
		log.debug("Fetched news links: {}", newsLinks);

		for (NewsResponseDTO newsResponseDTO : newsLinks) {
			if (!isNewsNeedProcessed(newsResponseDTO.getLink(), stockName, newsResponseDTO.getDate())) {
				continue;
			}

			Optional<GPTNewsDomain> optionalGPTNewsDomain = processAnalyzeNewsLink(newsResponseDTO.getLink(),
				newsResponseDTO.getDate());

			if (optionalGPTNewsDomain.isPresent()) {
				String code = stockMap.get(optionalGPTNewsDomain.get().getStockName());
				optionalGPTNewsDomain.get().addStockCode(code);
				gptStockNewsRepository.save(optionalGPTNewsDomain.get());
			}
		}

		return newsLinks.stream()
			.map(link -> gptStockNewsRepository.findByLink(link.getLink()))
			.flatMap(Optional::stream)
			.filter(GPTNewsDomain::isRelated)
			.filter(t -> t.getStockName().equals(stockName))
			.toList();
	}

	private void setStockCodeToGPTNewsDomain(List<GPTNewsDomain> target, Map<String, String> stockMap) {
		for (GPTNewsDomain gptNewsDomain : target) {
			String code = stockMap.getOrDefault(gptNewsDomain.getStockName(), "nil");
			gptNewsDomain.addStockCode(code);
			gptStockNewsRepository.save(gptNewsDomain);
		}
	}

	private Map<String, String> getStockDomainToHash() {
		List<Stock> stocks = stockRepository.findAll();
		Map<String, String> stockMap = new HashMap<>();

		for (Stock stock : stocks) {
			stockMap.put(stock.getName(), stock.getCode());
		}
		return stockMap;
	}

	@AnalyzeBackground
	public List<GPTNewsDomain> analyzeRankingStock(RankingStocksDTO command) {
		List<GPTNewsDomain> rankingNews = new ArrayList<>();
		AnalyzeBestNews analyzeBestNews = new AnalyzeBestNews();
		for (String stockName : command.getRankingStocks()) {
			if (!redisMarketRankAdapter.isExistInCache(stockName)) {
				log.info("analyze-ranking-stock start: {}", stockName);
				List<GPTNewsDomain> gptNewsDomains = this.analyzeStockNewsByDateWithStockName(command.getBaseAt(), stockName);
				rankingNews.addAll(gptNewsDomains);

				Optional<GPTNewsDomain> optBestNews = analyzeBestNews.getBestNews(gptNewsDomains);// 가장 좋은 뉴스를 찾아서 처리
				optBestNews.ifPresent(redisMarketRankAdapter::updateRankingNews);
			}else{
				log.info("analyze-ranking-stock already exist: {}", stockName);
				redisMarketRankAdapter.updateRankingNewsByStockName(stockName);
			}
		}

		return rankingNews;
	}

	private Optional<GPTNewsDomain> processAnalyzeNewsLink(String newsLink, LocalDate date) {
		log.info("Processing news link: {}", newsLink);
		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(newsLink, date);

		if (originalNews.isEmpty()) {
			log.error("Failed to fetch news body for link: {}", newsLink);
			return Optional.empty();
		}

		return gptNewsAdapter.findStockRaiseReason(originalNews.get(), originalNews.get().getTitle(),
			originalNews.get().getPubDate());
	}

	private List<NewsResponseDTO> fetchNewsForStock(LocalDate date, String stockName) {
		return pythonSearchServerAdapter.fetchNews(new NewsCommand(stockName, date, LocalDate.now()));
	}

	private boolean isNewsNeedProcessed(String newsLink, String stockName, LocalDate date) {
		boolean empty = gptStockNewsRepository.findByLink(newsLink).isEmpty();
		empty = empty & gptStockNewsRepository.findByStockNameAndPubDateWithRelatedIsTrue(stockName, date).isEmpty();
		log.debug("News is already processed: {} {}", newsLink, !empty);
		return empty;
	}

	private OriginalNews createOriginalNewsFromResponse(String newsLink, ParseNewsContentResponseDTO parsedContent) {
		return new OriginalNews(
			parsedContent.getTitle(),
			newsLink,
			parsedContent.getImgLink(),
			parsedContent.getPublishDate(),
			parsedContent.getText()
		);
	}
}
