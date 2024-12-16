package com.bjcareer.search.application.information;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.S3Service;
import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotTopicService {
	private final MarketRankingPort marketRankingPort;
	private final DocumentAnalyzeRepository documentAnalyzeRepository;
	private final S3Service s3Service;

	public List<GPTStockNewsDomain> getTrendingStory() {
		List<GPTStockNewsDomain> rankingNews = marketRankingPort.getRankingNews();
		rankingNews.forEach(it -> {
			String preSignedStockLogoUrl = s3Service.getStockLogoURL(it.getStockName());
			it.linkPreSignedStockLogoUrl(preSignedStockLogoUrl);
		});

		for (GPTStockNewsDomain gptStockNewsDomain : rankingNews) {
			List<String> themas = documentAnalyzeRepository.getThemasOfNews(
				gptStockNewsDomain.getNews().getOriginalLink(), gptStockNewsDomain.getStockName());

			gptStockNewsDomain.addThema(themas);

			String preSignedStockLogoUrl = s3Service.getStockLogoURL(gptStockNewsDomain.getStockName());
			gptStockNewsDomain.linkPreSignedStockLogoUrl(preSignedStockLogoUrl);
		}

		return rankingNews;
	}
}
