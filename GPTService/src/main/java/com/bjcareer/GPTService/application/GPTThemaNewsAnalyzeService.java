package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.application.port.in.AnalyzeThemaNewsCommand;
import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTThemaAdapter;
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
		List<NewsResponseDTO> newsResponseDTOS = fetchNewsForThem(command.getDate(), command.getKeyword());

		newsResponseDTOS.stream()
			.filter(t -> isNeedToUpdateBackground(command.getKeyword(), t.getDate()))
			.map(t -> pythonSearchServerAdapter.fetchNewsBody(t.getLink(), t.getDate()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(t -> gptThemaAdapter.summaryThemaNews(t, command.getKeyword(), GPTThemaAdapter.CUSTOM_MODEL))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(gptThemaNewsRepository::save)
			.filter(GPTThema::isRelatedThema)
			.map(t -> gptThemaAdapter.summaryThemaNews(t.getNews(), command.getKeyword(), GPTThemaAdapter.GPT_4o))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.limit(5)
			.forEach(gptThemaNewsRepository::save);

		return gptThemaNewsRepository.findThemaNews(command.getKeyword()).stream().toList();
	}

	public List<GPTThema> getAnalyzeThemaNews(String themaName, LocalDate startDate, LocalDate endDate) {
		List<GPTThema> result = new ArrayList<>();

		while (startDate.isBefore(endDate)) {
			log.debug("Analyze thema news for {} at {}", themaName, startDate);
			startDate = startDate.plusDays(1);

			List<GPTThema> themaNewsByPubDate = gptThemaNewsRepository.findThemaNewsByPubDate(themaName,
				startDate.atStartOfDay(),
				startDate.plusDays(1).atStartOfDay());

			if (themaNewsByPubDate.isEmpty()) {
				themaNewsByPubDate = analyzeThemaNewsByNewsLink(
					new AnalyzeThemaNewsCommand(themaName, startDate));
			}

			result.addAll(themaNewsByPubDate
				.stream()
				.filter(t -> t.getUpcomingDate().isPresent())
				.limit(10)
				.toList());
		}

		return result;
	}

	private boolean isNeedToUpdateBackground(String themaName, LocalDate date) {
		List<GPTThema> themaNewsByPubDate = gptThemaNewsRepository.findThemaNewsByPubDate(themaName,
			date.atStartOfDay(), date.plusDays(1).atStartOfDay());
		return themaNewsByPubDate.isEmpty();
	}

	private List<NewsResponseDTO> fetchNewsForThem(LocalDate date, String keyword) {

		return pythonSearchServerAdapter.fetchNews(
			new NewsCommand(keyword, date, LocalDate.now(AppConfig.ZONE_ID)));
	}
}
