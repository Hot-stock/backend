package com.bjcareer.search.application.search;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.GPTAPIPort;
import com.bjcareer.search.application.port.out.LoadNewsPort;
import com.bjcareer.search.application.port.out.NewsCommand;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.persistence.repository.chart.StockChartRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
	private final LoadNewsPort loadNewsPort;
	private final GPTAPIPort gptAPIPort;
	private final StockChartRepositoryAdapter stockChartRepositoryAdapter;

	@Transactional
	public Optional<GTPNewsDomain> findNewsToChartByDate(String stockName, LocalDate date) {
		StockChart chart = stockChartRepositoryAdapter.findChartByDate(stockName, date);
		Optional<GTPNewsDomain> optGtpNewsDomain = chart.loadNewByDate(date);

		if (optGtpNewsDomain.isPresent()) {
			log.info("News already found for {} {}", stockName, date);
			return optGtpNewsDomain;
		}

		log.debug("Linking news to OHLC. stockName: {}, date: {}", stockName, date);
		NewsCommand config = new NewsCommand(stockName, date, date);

		List<News> news = loadNewsPort.fetchNews(config); //뉴스 가지고 옴

		log.debug("찾아진 뉴스 개수는? {}", news.size());
		optGtpNewsDomain = linkNewsToOhlc(stockName, news, date);

		if (optGtpNewsDomain.isPresent()) {
			GTPNewsDomain gtpNewsDomain = optGtpNewsDomain.get();
			log.info("News found for {} {} {}", stockName, gtpNewsDomain.getReason(),
				gtpNewsDomain.getNews().getLink());
			chart.addNewsToOhlc(gtpNewsDomain, date);

			//링크해주는거 필요함
		} else {
			log.warn("No news found for {} {}", stockName, date);
		}

		stockChartRepositoryAdapter.updateStockChartOfOHLC(chart); //업데이트 되는지 확인
		return optGtpNewsDomain;
	}

	private Optional<GTPNewsDomain> linkNewsToOhlc(String stockName, List<News> news, LocalDate targetDate) {
		for (News n : news) {
			log.debug("Checking news for {} {}", n.getPubDate(), targetDate);
			if (isSameDate(targetDate, n)) {
				log.debug("Find news for {} {}", stockName, targetDate);
				Optional<GTPNewsDomain> stockRaiseReason = gptAPIPort.findStockRaiseReason(n.getContent(), stockName,
					targetDate);

				if (stockRaiseReason.isPresent()) {
					GTPNewsDomain gtpNews = stockRaiseReason.get(); //오타 있음

					if (isSameStock(stockName, gtpNews)) {
						gtpNews.addNewsDomain(n);
						return Optional.of(gtpNews);
					} else {
						log.warn("Stock name is not matched. stockName: {}, newsStockName: {}", stockName,
							gtpNews.getStockName());
					}
				}
			}
		}

		return Optional.empty();
	}

	private boolean isSameStock(String stockName, GTPNewsDomain gtpNews) {
		return gtpNews.getStockName().equals(stockName);
	}

	private boolean isSameDate(LocalDate targetDate, News n) {
		return n.getPubDate().equals(targetDate);
	}
}
