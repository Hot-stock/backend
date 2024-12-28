package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.application.port.in.AnalyzeThemaNewsCommand;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTThemaAdapter;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

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

		originalNews.stream()
			.filter(t -> gptThemaNewsRepository.findByLink(t.getNewsLink()).isEmpty())
			.map(t -> gptThemaAdapter.summaryThemaNews(t, command.getKeyword(), GPTThemaAdapter.CUSTOM_MODEL))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(gptThemaNewsRepository::save)
			.filter(GPTThema::isRelatedThema)
			.map(t -> gptThemaAdapter.summaryThemaNews(t.getNews(), command.getKeyword(), GPTThemaAdapter.GPT_4o))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(gptThemaNewsRepository::save);

		List<String> links = originalNews.stream().map(OriginalNews::getNewsLink).toList();
		return gptThemaNewsRepository.findByLinkIn(links);
	}

	private List<OriginalNews> fetchNewsForThem(LocalDate date, String keyword) {
		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, date, LocalDate.now(AppConfig.ZONE_ID)));

		return newsResponseDTOS.stream()
			.map(n -> pythonSearchServerAdapter.fetchNewsBody(n.getLink(), date))
			.flatMap(Optional::stream)
			.toList();
	}
}
