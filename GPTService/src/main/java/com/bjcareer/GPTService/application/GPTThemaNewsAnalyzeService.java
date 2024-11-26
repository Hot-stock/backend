package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.application.port.in.AnalyzeThemaNewsCommand;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.GPTThemaAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTThemaNewsAnalyzeService {
	private final GPTThemaAdapter gptThemaAdapter;
	private final PythonSearchServerAdapter pythonSearchServerAdapter;
	private final GPTThemaNewsRepository gptThemaNewsRepository;

	public List<GPTThema> analyzeThemaNewsByNewsLink(AnalyzeThemaNewsCommand command) {
		List<OriginalNews> originalNews = fetchNewsForThem(command.getDate(), command.getKeyword());
		return fetchThemaFromNewsAndSaveDB(originalNews, "오세훈 이재명");
	}

	private List<OriginalNews> fetchNewsForThem(LocalDate date, String keyword) {
		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, date, date));

		return newsResponseDTOS.stream()
			.map(n -> pythonSearchServerAdapter.fetchNewsBody(n.getLink()))
			.flatMap(Optional::stream)
			.toList();
	}

	private List<GPTThema> fetchThemaFromNewsAndSaveDB(List<OriginalNews> originalNews, String knownThema) {
		return originalNews.stream()
			.map(news -> gptThemaAdapter.summaryThemaNews(news, knownThema))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(gptThemaNewsRepository::save)
			.filter(GPTThema::isRelatedThema)
			.toList();
	}
}
