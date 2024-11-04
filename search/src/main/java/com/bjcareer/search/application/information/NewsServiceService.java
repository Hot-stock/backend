package com.bjcareer.search.application.information;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.NewsServiceUsecase;
import com.bjcareer.search.application.port.out.GPTAPIPort;
import com.bjcareer.search.application.port.out.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.LoadNewsPort;
import com.bjcareer.search.application.port.out.NewsCommand;
import com.bjcareer.search.application.port.out.StockChartRepositoryPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.StockRaiseReasonEntity;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.persistence.repository.gpt.StockRaiseRepository;
import com.bjcareer.search.out.persistence.repository.stock.StockRepository;
import com.bjcareer.search.out.persistence.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.out.persistence.repository.stock.ThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceService implements NewsServiceUsecase {
	private final LoadNewsPort loadNewsPort;
	private final StockRepository stockRepository;
	private final ThemaRepository themaRepository;
	private final ThemaInfoRepository themaInfoRepository;
	private final StockRaiseRepository stockRaiseRepository;
	private final GPTAPIPort gptAPIPort;
	private final StockChartRepositoryPort stockChartRepositoryPort;

	@Override
	@Transactional
	public Optional<GTPNewsDomain> findRaiseReasonThadDate(String stockName, LocalDate date) {
		LoadChartSpecificDateCommand command = new LoadChartSpecificDateCommand(stockName, date);

		StockChart chart = stockChartRepositoryPort.findChartByDate(command);
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

		stockChartRepositoryPort.updateStockChartOfOHLC(chart);
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

	@Transactional
	public Map<LocalDate, GTPNewsDomain> finNextSchedule(String stockName) {
		Stock stock = validationStock(stockName);
		NewsCommand newsCommand = new NewsCommand(stockName, LocalDate.now().minusDays(1),
			LocalDate.now());
		List<News> news = loadNewsPort.fetchNews(newsCommand);
		Map<LocalDate, GTPNewsDomain> map = extracteNewsByDate(stockName, news);

		for (LocalDate date : map.keySet()) {
			GTPNewsDomain gtpNewsDomain = map.get(date);

			log.info("date = {}, gtpNewsDomain = {} ", date, gtpNewsDomain);

			Optional<ThemaInfo> optThemaInfo = themaInfoRepository.findByName(gtpNewsDomain.getThema());

			if (optThemaInfo.isEmpty()) {
				ThemaInfo themaInfo = new ThemaInfo(gtpNewsDomain.getThema());
				optThemaInfo = Optional.of(themaInfoRepository.save(themaInfo));
			}

			StockRaiseReasonEntity stockRaiseReasonEntity = new StockRaiseReasonEntity(stock, optThemaInfo.get(),
				gtpNewsDomain.getReason(), gtpNewsDomain.getNews().getLink(),
				gtpNewsDomain.getNextReason(), gtpNewsDomain.getNext(), gtpNewsDomain.getNews().getPubDate());

			stockRaiseRepository.save(stockRaiseReasonEntity);

			Thema thema = new Thema(stock, optThemaInfo.get());
			Optional<Thema> byStockNameAndThemaName = themaRepository.findByStockNameAndThemaName(stockName,
				gtpNewsDomain.getThema());

			if (byStockNameAndThemaName.isEmpty()) {
				themaRepository.save(thema);
			}

			log.debug("stockRaiseReasonEntity = " + stockRaiseReasonEntity);
		}

		return map;
	}

	private Map<LocalDate, GTPNewsDomain> extracteNewsByDate(String stockName, List<News> newsDomains) {
		Map<LocalDate, GTPNewsDomain> dateMap = new HashMap<>();

		for (News news : newsDomains) {
			LocalDate pubDate = news.getPubDate();

			if (dateMap.containsKey(pubDate)) {
				continue;
			}

			Optional<GTPNewsDomain> stockReason = gptAPIPort.findStockRaiseReason(news.getContent(), stockName, pubDate);
			stockReason.ifPresent(gtpNewsDomain -> {
				gtpNewsDomain.addNewsDomain(news);
				dateMap.put(pubDate, gtpNewsDomain);
			});
		}

		return dateMap;
	}

	private Stock validationStock(String stockName) {
		Optional<Stock> optStock = stockRepository.findByName(stockName);
		return optStock.orElseThrow(() -> new IllegalArgumentException("stock is null"));
	}
}
