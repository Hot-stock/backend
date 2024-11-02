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
					stocks.add(new Stock(stockCode, stockName));
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
