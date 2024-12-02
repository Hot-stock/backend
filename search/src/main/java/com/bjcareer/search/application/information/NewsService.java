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
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

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
	private final ThemaInfoRepositoryPort themaInfoRepositoryPort;
	private final ThemaRepositoryPort themaRepositoryPort;

	private final DocumentAnalyzeRepository documentAnalyzeRepository;

	private final GPTNewsPort gptNewsPort;

	@Override
	public List<GPTNewsDomain> findRaiseReasonThatDate(String stockName, LocalDate date) {
		Stock stock = CommonValidations.validationStock(stockRepositoryPort, stockName);
		StockChart chart = CommonValidations.validationStockChart(stockChartRepositoryPort, stock.getCode());

		List<GPTNewsDomain> GPTNewsDomains = fetchOhlcFromApiIfMissing(stockName, date, chart, stock);

		if (!GPTNewsDomains.isEmpty()) {
			log.info("News already found for {} {}", stockName, date);
			return GPTNewsDomains;
		}

		log.debug("Linking news to OHLC. stockName: {}, date: {}", stockName, date);
		NewsCommand config = new NewsCommand(stockName, date, date);

		List<News> news = loadNewsPort.fetchNews(config);

		GPTNewsDomains = changeNewsToGPTNews(stockName, news, date);

		if (GPTNewsDomains.isEmpty()) {
			log.warn("No news found for {} {}", stockName, date);
		} else {
			log.info("News found for {} {}", stockName, date);

			linkNewsToChartAndSaveThema(date, GPTNewsDomains, chart, stock);

			stockChartRepositoryPort.updateStockChartOfOHLC(chart);
		}

		return GPTNewsDomains;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GPTNewsDomain> findNextSchedule(String stockName, LocalDate date) {
		Stock stock = CommonValidations.validationStock(stockRepositoryPort, stockName);
		StockChart chart = CommonValidations.validationStockChart(stockChartRepositoryPort, stock.getCode());

		List<GPTNewsDomain> allNews = chart.getAllNews();

		if (allNews.isEmpty()) {
			log.info("No news found for {}", stockName);
			return List.of();
		}

		List<GPTNewsDomain> nextSchedule = chart.getNextSchedule(date);
		log.info("Next schedule found for {} {}", stockName, nextSchedule.size());
		return nextSchedule;
	}

	@Override
	public List<GPTNewsDomain> searchThemaNews(String Keyword, LocalDate date) {
		// List<News> news = loadNewsPort.fetchNews(new NewsCommand(Keyword, date, date));
		// gptNewsPort.findStockRaiseReason(news.get(0).getContent(), Keyword, date);

		return null;
	}

	private void linkNewsToChartAndSaveThema(LocalDate date, List<GPTNewsDomain> GPTNewsDomains, StockChart chart,
		Stock stock) {
		for (GPTNewsDomain gtpNews : GPTNewsDomains) {
			chart.addNewsToOhlc(gtpNews, date);

			List<GPTNewsDomain.GPTThema> themas = gtpNews.getThemas();

			for (GPTNewsDomain.GPTThema thema : themas) {
				ThemaInfo themaInfo = themaInfoRepositoryPort.findByName(thema.getName())
					.orElseGet(() -> themaInfoRepositoryPort.save(new ThemaInfo(thema.getName())));

				themaRepositoryPort.findByName(thema.getName(), stock.getName())
					.orElseGet(() -> themaRepositoryPort.save(new Thema(stock, themaInfo)));
			}
		}
	}

	private List<GPTNewsDomain> fetchOhlcFromApiIfMissing(String stockName, LocalDate date, StockChart chart,
		Stock stock) {
		List<GPTNewsDomain> GPTNewsDomains = new ArrayList<>();

		try{
			GPTNewsDomains = chart.loadNewByDate(date);
		}catch (NoResultException e) {
			log.warn("Can't found ohlc data so download ohlc {} {}", stockName, date);

			StockChart tempChart = loadStockInformationServerPort.loadStockChart(
				new StockChartQueryCommand(stock, date, date));

			chart.addOHLC(tempChart.getOhlcList());
		}

		return GPTNewsDomains;
	}

	private List<GPTNewsDomain> changeNewsToGPTNews(String stockName, List<News> news, LocalDate targetDate) {
		List<GPTNewsDomain> GPTNewsDomains = new ArrayList<>();

		for (News n : news) {
			log.debug("Checking news for {} {}", n.getPubDate(), targetDate);
			if (isSameDate(targetDate, n)) {
				log.debug("Find news for and query to gpt{} {} {}", stockName, n.getPubDate(), n.getOriginalLink());
				Optional<GPTNewsDomain> stockRaiseReason = gptNewsPort.findStockRaiseReason(n.getContent(), stockName,
					targetDate);

				if (stockRaiseReason.isPresent()) {
					GPTNewsDomain gtpNews = stockRaiseReason.get();

					if (isSameStock(stockName, gtpNews)) {
						gtpNews.addNewsDomain(n);
						GPTNewsDomains.add(gtpNews);
					} else {
						log.warn("Stock name is not matched. stockName: {}, newsStockName: {}", stockName,
							gtpNews.getStockName());
					}
				}
			}
		}

		return GPTNewsDomains;
	}

	private boolean isSameStock(String stockName, GPTNewsDomain gtpNews) {
		return gtpNews.getStockName().equals(stockName);
	}

	private boolean isSameDate(LocalDate targetDate, News n) {
		return n.getPubDate().equals(targetDate);
	}

}
