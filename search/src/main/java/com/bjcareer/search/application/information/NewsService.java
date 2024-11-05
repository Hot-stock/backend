package com.bjcareer.search.application.information;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.NewsServiceUsecase;
import com.bjcareer.search.application.port.out.api.GPTAPIPort;
import com.bjcareer.search.application.port.out.api.LoadNewsPort;
import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NewsService implements NewsServiceUsecase {
	private final LoadNewsPort loadNewsPort;
	private final LoadStockInformationPort loadStockInformationServerPort;

	private final StockRepositoryPort stockRepositoryPort;
	private final StockChartRepositoryPort stockChartRepositoryPort;

	private final GPTAPIPort gptAPIPort;

	@Override
	public Optional<GTPNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date) {
		Stock stock = validationStock(stockName);
		StockChart chart = stockChartRepositoryPort.loadStockChart(stock.getCode());

		if (chart.loadNewByDate(date).isEmpty()) {
			log.info("Can't found ohlc data so download ohlc {} {}", stockName, date);

			StockChart tempChart = loadStockInformationServerPort.loadStockChart(
				new StockChartQueryCommand(stock, date, date));
			chart.addOHLC(tempChart.getOhlcList());
		}

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
		} else {
			log.warn("No news found for {} {}", stockName, date);
		}

		stockChartRepositoryPort.updateStockChartOfOHLC(chart);
		return optGtpNewsDomain;
	}

	@Transactional(readOnly = true)
	public List<GTPNewsDomain> findNextSchedule(String stockName, LocalDate date) {
		Stock stock = validationStock(stockName);

		StockChart chart = stockChartRepositoryPort.loadStockChart(stock.getCode());
		List<GTPNewsDomain> allNews = chart.getAllNews();

		if (allNews.isEmpty()) {
			log.info("No news found for {}", stockName);
			return List.of();
		}

		List<GTPNewsDomain> nextSchedule = chart.getNextSchedule(date);
		log.info("Next schedule found for {} {}", stockName, nextSchedule.size());
		return nextSchedule;
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

	private Stock validationStock(String stockName) {
		Optional<Stock> optStock = stockRepositoryPort.findByName(stockName);
		return optStock.orElseThrow(() -> new IllegalArgumentException("stock is null"));
	}

	private boolean isSameStock(String stockName, GTPNewsDomain gtpNews) {
		return gtpNews.getStockName().equals(stockName);
	}

	private boolean isSameDate(LocalDate targetDate, News n) {
		return n.getPubDate().equals(targetDate);
	}

}
