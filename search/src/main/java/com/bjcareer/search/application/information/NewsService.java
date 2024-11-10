package com.bjcareer.search.application.information;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.CommonValidations;
import com.bjcareer.search.application.port.in.NewsServiceUsecase;
import com.bjcareer.search.application.port.out.api.GPTNewsPort;
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

import jakarta.persistence.NoResultException;
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

	private final GPTNewsPort gptNewsPort;

	@Override
	public List<GTPNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date) {
		Stock stock = CommonValidations.validationStock(stockRepositoryPort, stockName);
		StockChart chart = CommonValidations.validationStockChart(stockChartRepositoryPort, stock.getCode());

		List<GTPNewsDomain> gtpNewsDomains = fetchOhlcFromApiIfMissing(stockName, date, chart, stock);

		if (!gtpNewsDomains.isEmpty()) {
			log.info("News already found for {} {}", stockName, date);
			return gtpNewsDomains;
		}

		log.debug("Linking news to OHLC. stockName: {}, date: {}", stockName, date);
		NewsCommand config = new NewsCommand(stockName, date, date);

		List<News> news = loadNewsPort.fetchNews(config);

		log.debug("찾아진 뉴스 개수는? {}", news.size());
		gtpNewsDomains = changeNewsToGPTNews(stockName, news, date);

		if (gtpNewsDomains.isEmpty()) {
			log.warn("No news found for {} {}", stockName, date);
		} else {
			log.info("News found for {} {}", stockName, date);

			for (GTPNewsDomain gtpNews : gtpNewsDomains) {
				chart.addNewsToOhlc(gtpNews, date);
			}

			stockChartRepositoryPort.updateStockChartOfOHLC(chart);
		}

		return gtpNewsDomains;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GTPNewsDomain> findNextSchedule(String stockName, LocalDate date) {
		Stock stock = CommonValidations.validationStock(stockRepositoryPort, stockName);
		StockChart chart = CommonValidations.validationStockChart(stockChartRepositoryPort, stock.getCode());

		List<GTPNewsDomain> allNews = chart.getAllNews();

		if (allNews.isEmpty()) {
			log.info("No news found for {}", stockName);
			return List.of();
		}

		List<GTPNewsDomain> nextSchedule = chart.getNextSchedule(date);
		log.info("Next schedule found for {} {}", stockName, nextSchedule.size());
		return nextSchedule;
	}

	@Override
	public List<GTPNewsDomain> saveThemaNews(String Keyword, LocalDate date) {
		return List.of();
	}

	private List<GTPNewsDomain> fetchOhlcFromApiIfMissing(String stockName, LocalDate date, StockChart chart,
		Stock stock) {
		List<GTPNewsDomain> gtpNewsDomains = new ArrayList<>();

		try{
			gtpNewsDomains = chart.loadNewByDate(date);
		}catch (NoResultException e) {
			log.warn("Can't found ohlc data so download ohlc {} {}", stockName, date);

			StockChart tempChart = loadStockInformationServerPort.loadStockChart(
				new StockChartQueryCommand(stock, date, date));

			chart.addOHLC(tempChart.getOhlcList());
		}

		return gtpNewsDomains;
	}

	private List<GTPNewsDomain> changeNewsToGPTNews(String stockName, List<News> news, LocalDate targetDate) {
		List<GTPNewsDomain> gtpNewsDomains = new ArrayList<>();

		for (News n : news) {
			log.debug("Checking news for {} {}", n.getPubDate(), targetDate);
			if (isSameDate(targetDate, n)) {
				log.debug("Find news for and query to gpt{} {} {}", stockName, targetDate, n.getLink());
				Optional<GTPNewsDomain> stockRaiseReason = gptNewsPort.findStockRaiseReason(n.getContent(), stockName,
					targetDate);

				if (stockRaiseReason.isPresent()) {
					GTPNewsDomain gtpNews = stockRaiseReason.get();

					if (isSameStock(stockName, gtpNews)) {
						gtpNews.addNewsDomain(n);
						gtpNewsDomains.add(gtpNews);
					} else {
						log.warn("Stock name is not matched. stockName: {}, newsStockName: {}", stockName,
							gtpNews.getStockName());
					}
				}
			}
		}

		return gtpNewsDomains;
	}

	private boolean isSameStock(String stockName, GTPNewsDomain gtpNews) {
		return gtpNews.getStockName().equals(stockName);
	}

	private boolean isSameDate(LocalDate targetDate, News n) {
		return n.getPubDate().equals(targetDate);
	}

}
