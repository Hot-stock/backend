package com.bjcareer.search.application.information;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.S3Service;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NextEventService {
	private final DocumentAnalyzeRepository documentAnalyzeRepository;
	private final StockRepositoryPort stockRepositoryPort;
	private final S3Service s3Service;

	public PaginationDomain<GPTStockNewsDomain> getUpcomingEvents(int page, int size) {
		PaginationDomain<GPTStockNewsDomain> upcomingNews = documentAnalyzeRepository.getUpcomingNews(page, size);
		upcomingNews.getContent().forEach(news -> {
			String imageUrl = s3Service.getStockLogoURL(news.getStockName());
			news.linkPreSignedStockLogoUrl(imageUrl);
			List<String> themasOfNews = documentAnalyzeRepository.getThemasOfNews(news.getNews().getOriginalLink(),
				news.getStockName());
			news.addThema(themasOfNews);
		});
		return upcomingNews;
	}

	public List<GPTStockNewsDomain> filterUpcomingEventsByStockName(String stockCode) {
		Optional<Stock> byCode =
			stockRepositoryPort.findByCode(stockCode);
		if (byCode.isEmpty()) {
			return List.of();
		}

		return documentAnalyzeRepository.getUpcomingNewsByStockName(byCode.get().getName());
	}
}
