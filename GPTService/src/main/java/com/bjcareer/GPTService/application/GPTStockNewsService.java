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

@Service
@RequiredArgsConstructor
public class GPTStockNewsService {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTNewsAdapter gptNewsAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;

	/**
	 * Handles asynchronous requests, typically triggered via Kafka.
	 */
	public GPTNewsDomain saveGPTStockNews(AnalyzeStockNewsCommand command) {
		return gptStockNewsRepository.findByLink(command.newLink)
			.or(() -> processNewGPTStockNews(command))
			.orElseThrow(() -> new IllegalStateException("Failed to save GPT stock news"));
	}

	//동기식 요청을 처리하기 위한 함수 당일 한정 사용
	public List<GPTNewsDomain> analyzeStockNews(LocalDate date, String stockName) {
		List<NewsResponseDTO> newsLinks = fetchNewsForStock(date, stockName);

		newsLinks.stream()
			.filter(this::isNewsNotProcessed)
			.map(this::processNewsLink)
			.flatMap(Optional::stream)
			.map(gptStockNewsRepository::save)
			.toList();

		return newsLinks.stream()
			.map(link -> gptStockNewsRepository.findByLink(link.getLink()))
			.flatMap(Optional::stream)
			.toList();
	}

	private Optional<GPTNewsDomain> processNewsLink(NewsResponseDTO newsResponse) {
		ParseNewsContentResponseDTO parsedContent = pythonSearchServerAdapter.fetchNewsBody(newsResponse.getLink());
		OriginalNews originalNews = createOriginalNewsFromResponse(newsResponse, parsedContent);
		return gptNewsAdapter.findStockRaiseReason(originalNews, originalNews.getTitle(), originalNews.getPubDate());
	}

	private Optional<GPTNewsDomain> processNewGPTStockNews(AnalyzeStockNewsCommand command) {
		OriginalNews originalNews = createOriginalNews(command);
		Optional<GPTNewsDomain> gptNewsDomain = gptNewsAdapter.findStockRaiseReason(
			originalNews, command.stockName, originalNews.getPubDate());
		gptNewsDomain.ifPresent(gptStockNewsRepository::save);
		return gptNewsDomain;
	}

	private List<NewsResponseDTO> fetchNewsForStock(LocalDate date, String stockName) {
		return pythonSearchServerAdapter.fetchNews(new NewsCommand(stockName, date, date));
	}

	private boolean isNewsNotProcessed(NewsResponseDTO newsResponse) {
		return gptStockNewsRepository.findByLink(newsResponse.getLink()).isEmpty();
	}

	private OriginalNews createOriginalNews(AnalyzeStockNewsCommand command) {
		return new OriginalNews(
			command.title,
			command.newLink,
			command.imgLink,
			command.pubDate,
			command.content
		);
	}

	private OriginalNews createOriginalNewsFromResponse(NewsResponseDTO newsResponse,
		ParseNewsContentResponseDTO parsedContent) {
		return new OriginalNews(
			parsedContent.getTitle(),
			newsResponse.getLink(),
			parsedContent.getImgLink(),
			parsedContent.getPublishDate(),
			parsedContent.getText()
		);
	}
}
