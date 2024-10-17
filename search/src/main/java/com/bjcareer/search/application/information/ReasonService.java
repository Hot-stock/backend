package com.bjcareer.search.application.information;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.ReasonUsecase;
import com.bjcareer.search.application.port.out.GPTAPIPort;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.NewsDomain;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockRaiseReasonEntity;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.api.gpt.ChatGPTAdapter;
import com.bjcareer.search.out.api.naver.ApiNaverNews;
import com.bjcareer.search.repository.gpt.StockRaiseRepository;
import com.bjcareer.search.repository.stock.StockRepository;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.repository.stock.ThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReasonService implements ReasonUsecase {
	private final ApiNaverNews apiNaverNews;
	private final StockRepository stockRepository;
	private final ThemaRepository themaRepository;
	private final ThemaInfoRepository themaInfoRepository;
	private final StockRaiseRepository stockRaiseRepository;
	private final GPTAPIPort port;

	@Transactional
	public Map<LocalDate, GTPNewsDomain> findSearchRaiseReason(String stockName) {
		Stock stock = validationStock(stockName);

		List<NewsDomain> newsDomains = apiNaverNews.fetchNews("특징주 " + stockName);
		Map<LocalDate, GTPNewsDomain> map = extracteNewsByDate(stockName, newsDomains);

		for (LocalDate date : map.keySet()) {
			GTPNewsDomain gtpNewsDomain = map.get(date);

			log.info("date = {}, gtpNewsDomain = {} ", date, gtpNewsDomain);

			Optional<ThemaInfo> optThemaInfo = themaInfoRepository.findByName(gtpNewsDomain.getThema());

			if (optThemaInfo.isEmpty()) {
				ThemaInfo themaInfo = new ThemaInfo(gtpNewsDomain.getThema());
				optThemaInfo = Optional.of(themaInfoRepository.save(themaInfo));
			}

			StockRaiseReasonEntity stockRaiseReasonEntity = new StockRaiseReasonEntity(stock, optThemaInfo.get(),
				gtpNewsDomain.getReason(), gtpNewsDomain.getNewsDomain().getLink(),
				gtpNewsDomain.getNextReason(), gtpNewsDomain.getNext(), gtpNewsDomain.getNewsDomain().getPubDate());

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

	private Map<LocalDate, GTPNewsDomain> extracteNewsByDate(String stockName, List<NewsDomain> newsDomains) {
		Map<LocalDate, GTPNewsDomain> dateMap = new HashMap<>();

		for (NewsDomain news : newsDomains) {
			LocalDate pubDate = news.getPubDate();

			if (dateMap.containsKey(pubDate)) {
				continue;
			}

			Optional<String> content = news.getContent();

			if (content.isPresent()) {
				Optional<GTPNewsDomain> stockReason = port.findStockRaiseReason(content.get(), stockName, pubDate);
				stockReason.ifPresent(gtpNewsDomain -> {
					gtpNewsDomain.addNewsDomain(news);
					dateMap.put(pubDate, gtpNewsDomain);
				});
			} else {
				log.warn("content is empty");
			}
		}

		return dateMap;
	}

	private Stock validationStock(String stockName) {
		Optional<Stock> optStock = stockRepository.findByName(stockName);
		return optStock.orElseThrow(() -> new IllegalArgumentException("stock is null"));
	}
}
