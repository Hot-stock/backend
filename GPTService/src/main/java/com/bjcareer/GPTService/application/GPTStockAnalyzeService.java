package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.application.port.in.AnalyzeStockNewsCommand;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.api.python.ParseNewsContentResponseDTO;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTStockAnalyzeService {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTNewsAdapter gptNewsAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;

	/**
	 * Handles asynchronous requests, typically triggered via Kafka.
	 */
	public GPTNewsDomain analyzeStockNewsByNewsLink(AnalyzeStockNewsCommand command) {
		if (isNewsNotProcessed(command.getNewsLink())) {
			Optional<GPTNewsDomain> optionalGPTNewsDomain = processAnalyzeNewsLink(command.getNewsLink());
			optionalGPTNewsDomain.ifPresent(gptStockNewsRepository::save);
		}

		return gptStockNewsRepository.findByLink(command.getNewsLink()).orElseThrow();
	}

	//동기식 요청을 처리하기 위한 함수 당일 한정 사용
	public List<GPTNewsDomain> analyzeStockNewsByDateWithStockName(LocalDate date, String stockName) {
		log.debug("Analyzing stock news for date: {} stock: {}", date, stockName);
		List<NewsResponseDTO> newsLinks = fetchNewsForStock(date, stockName);
		log.debug("Fetched news links: {}", newsLinks);

		newsLinks.stream()
			.filter(n -> isNewsNotProcessed(n.getLink()))
			.map(n -> processAnalyzeNewsLink(n.getLink()))
			.flatMap(Optional::stream)
			.map(gptStockNewsRepository::save)
			.toList();

		return newsLinks.stream()
			.map(link -> gptStockNewsRepository.findByLink(link.getLink()))
			.flatMap(Optional::stream)
			.toList();
	}

	private Optional<GPTNewsDomain> processAnalyzeNewsLink(String newsLink) {
		log.info("Processing news link: {}", newsLink);
		ParseNewsContentResponseDTO parsedContent = pythonSearchServerAdapter.fetchNewsBody(newsLink);
		log.info("Parsed news content: {} {}", parsedContent.getTitle(), parsedContent.getPublishDate());
		OriginalNews originalNews = createOriginalNewsFromResponse(newsLink, parsedContent);
		log.info("Original news: {} {}", originalNews.getTitle(), originalNews.getPubDate());
		return gptNewsAdapter.findStockRaiseReason(originalNews, originalNews.getTitle(), originalNews.getPubDate());
	}

	private List<NewsResponseDTO> fetchNewsForStock(LocalDate date, String stockName) {
		return pythonSearchServerAdapter.fetchNews(new NewsCommand(stockName, date, date));
	}

	private boolean isNewsNotProcessed(String newsLink) {
		boolean empty = gptStockNewsRepository.findByLink(newsLink).isEmpty();
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
