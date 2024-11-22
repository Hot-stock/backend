package com.bjcareer.GPTService.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GPTStockNewsService {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTNewsAdapter gptNewsAdapter;

	public GPTNewsDomain saveGPTStockNews(SaveGPTStockNewsCommand command) {
		Optional<GPTNewsDomain> optionalGPTNewsDomain = gptStockNewsRepository.findByLink(command.link);

		if(optionalGPTNewsDomain.isEmpty()){
			OriginalNews originalNews = new OriginalNews(command.title, command.link, command.link, command.content, command.pubDate, command.content);
			optionalGPTNewsDomain = gptNewsAdapter.findStockRaiseReason(originalNews,
				command.stockName, originalNews.getPubDate());
			optionalGPTNewsDomain.ifPresent(gptStockNewsRepository::save);
		}

		return optionalGPTNewsDomain.get();
	}
}
