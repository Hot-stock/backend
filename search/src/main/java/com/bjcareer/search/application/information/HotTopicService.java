package com.bjcareer.search.application.information;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

		List<String> links = rankingNews.stream()
			.map(rankDomain -> rankDomain.getNews().getOriginalLink())
			.collect(Collectors.toList());

		List<GPTStockNewsDomain> stockNewsDomains = documentAnalyzeRepository.getStockNewsByLinks(links);

		for (GPTStockNewsDomain domain : stockNewsDomains) {
			String preSignedStockLogoUrl = s3Service.getStockLogoURL(domain.getStockName());
			domain.linkPreSignedStockLogoUrl(preSignedStockLogoUrl);
		}

		return stockNewsDomains;
	}
}
