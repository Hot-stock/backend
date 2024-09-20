package com.bjcareer.search.out.crawling.naver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Component
public class CrawlingNaverFinance {
	private static final String BASE_URL = "https://finance.naver.com";
	private static final String THEME_URL = BASE_URL + "/sise/theme.naver?page=";
	private static final String STOCK_DETAIL_URL = BASE_URL + "/item/main.naver?code=";

	private Map<String, Stock> stocks = new HashMap<>();
	private Map<String, ThemaInfo> themaInfos = new HashMap<>();
	private Map<String, Thema> themas = new HashMap<>();

	public CrawlingNaverFinance(Map<String, Stock> stocks, Map<String, ThemaInfo> themaInfoMap,
		Map<String, Thema> themas) {
		this.stocks = stocks;
		this.themaInfos = themaInfoMap;
		this.themas = themas;
	}

	public void crawlingThema(Integer page) {
		log.debug("Executing in thread: {}, page: {}", Thread.currentThread().getName(), page);
		Map<String, Boolean> cache = new HashMap<>();
		List<ThemaInfo> themaList = getThemaInfoList(page);

		// 각 테마에 속한 종목 정보를 크롤링하고, 결과 목록에 추가
		for (ThemaInfo themaInfo : themaList) {
			themaInfos.putIfAbsent(themaInfo.getName(), themaInfo); // 중복 방지
			List<Stock> stockInfoList = getStockInfoList(themaInfo.getHref(), cache);

			for (Stock stock : stockInfoList) {
				stocks.putIfAbsent(stock.getCode(), stock); // 중복 방지
				Thema thema = new Thema(stocks.get(stock.getCode()), themaInfos.get(themaInfo.getName()));
				themas.putIfAbsent(thema.getKey(), thema); // 중복 방지
			}
		}
	}

	public Stock getStock(String stockCode, String stockName) {
		try {
			Document page = getDocument(STOCK_DETAIL_URL + stockCode);
			Market marketType = getMarketType(page);

			Long issuedShares = getLongValue(page, NaverFinanceCssQuery.GET_SHARED_STOCK);
			Long currentPrice = getLongValue(page, NaverFinanceCssQuery.GET_PRICE);

			return new Stock(stockCode, stockName, marketType, STOCK_DETAIL_URL + stockCode, issuedShares,
				currentPrice);
		} catch (NullPointerException | NumberFormatException e) {
			log.error("Error while extracting stock information: {}", e.getMessage());
			return new Stock(stockCode, stockName, null, null, null, null);
		}
	}

	// 테마 정보 목록을 가져오는 메서드
	private List<ThemaInfo> getThemaInfoList(int page) {
		List<ThemaInfo> themaList = new ArrayList<>();
		Elements themaElements = getElements(THEME_URL + page, NaverFinanceCssQuery.GET_THEMA_LIST);

		for (Element element : themaElements) {
			String themaName = element.text();
			String themaLink = BASE_URL + element.attr("href");
			themaList.add(new ThemaInfo(themaName, themaLink));
		}
		return themaList;
	}

	private List<Stock> getStockInfoList(String themaLink, Map<String, Boolean> cache) {
		List<Stock> stocks = new ArrayList<>();
		Elements stockElements = getElements(themaLink, NaverFinanceCssQuery.GET_STOCK_LIST);

		for (Element element : stockElements) {
			String stockLink = BASE_URL + element.attr("href");

			if (stockLink.contains(NaverFinanceCssQuery.GET_STOCK_LINK)) {
				String stockName = element.text();
				String stockCode = extractStockCode(stockLink);

				if (stockCode != null) {
					boolean cacheAble = cache.getOrDefault(stockCode, false);

					if (cacheAble) {
						continue;
					}
					
					cache.put(stockCode, true);
					Stock stock = getStock(stockCode, stockName);

					if (stock.validStock()) {
						stocks.add(stock);
					}
				}
			}
		}
		return stocks;
	}

	// 종목 코드 추출
	private String extractStockCode(String stockLink) {
		String divideTarget = "code=";
		String[] stockCodeArray = stockLink.split(divideTarget);
		return stockCodeArray.length > 1 ? stockCodeArray[1] : null;
	}

	// 시장 구분(KOSPI, KOSDAQ) 가져오는 메서드
	private Market getMarketType(Document stockPage) {
		// 네이버 금융의 새로운 구조에 맞는 선택자 확인
		Element marketElement = stockPage.selectFirst(NaverFinanceCssQuery.GET_MARKET_TYPE);

		if (marketElement != null) {
			String marketImgAlt = marketElement.attr("alt");
			if (marketImgAlt.contains("코스피"))
				return Market.KOSPI;
			else if (marketImgAlt.contains("코스닥"))
				return Market.KOSDAQ;
		}
		return null;
	}

	// Long 타입 값을 추출하는 메서드
	private Long getLongValue(Document document, String cssQuery) {
		Element element = document.selectFirst(cssQuery);

		try {
			return Long.parseLong(element.text().replace(",", ""));
		} catch (NumberFormatException e) {
			log.error("Error parsing long value for query '{}': {}", cssQuery, e.getMessage());
			throw e;
		}
	}

	// HTML 요소 리스트를 가져오는 공통 메서드
	private Elements getElements(String url, String cssQuery) {
		Document document = getDocument(url);
		return document.select(cssQuery);
	}

	private Document getDocument(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			log.error("Error while connecting to URL: " + url, e.getMessage());
			return new Document(url);
		}
	}
}
