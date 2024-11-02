package com.bjcareer.search.out.api.python;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.application.port.out.QueryStockServerPort;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PythonSearchServerAdapter implements QueryStockServerPort {
	@Value("${python-search.address}")
	private String address;

	private final RestTemplate restTemplate;

	public ParseNewsContentResponseDTO getNewsBody(String link) {
		log.debug("바디를 요청한 뉴스 링크는? : {}", link);
		String url = address + PythonServerURI.PARSE_CONTENT + link;

		ResponseEntity<ParseNewsContentResponseDTO> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			ParseNewsContentResponseDTO.class);

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return null;
		}

		ParseNewsContentResponseDTO body = exchange.getBody();
		return body;
	}

	@Override
	public StockChart loadStockChart(StockChartQueryConfig config) {
		String url = config.buildUrl(address + PythonServerURI.OHLC);

		ResponseEntity<List<OhlcResponseDTO>> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			new ParameterizedTypeReference<List<OhlcResponseDTO>>() {});

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return null;
		}

		List<OhlcResponseDTO> res = exchange.getBody();

		List<OHLC> ohlcs = res.stream().map(body -> {
			return new OHLC(body.getOpen(), body.getHigh(), body.getLow(), body.getClose(), body.getDate());
		}).toList();

		return new StockChart(config.getStock(), ohlcs);
	}

	@Override
	public List<Stock> loadStockInfo(Market market) {
		String href = "https://finance.naver.com/item/main.naver?code=";
		String url = address + PythonServerURI.MARKET + "?q=" + market.name();

		log.info("url: {}", url);

		ResponseEntity<List<MarketResponseDTO>> exchange = restTemplate.exchange(url, HttpMethod.GET, null,
			new ParameterizedTypeReference<List<MarketResponseDTO>>() {
			});

		HttpStatusCode statusCode = exchange.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			log.error("Failed to get news body from python server. Status code: {}", statusCode);
			return null;
		}

		List<MarketResponseDTO> res = exchange.getBody();
		return res.stream().map(body -> {
			return new Stock(body.getSymbol(), body.getName(), Market.fromString(body.getMarket()),
				href + body.getSymbol(), getIssuedShares(body)
				, body.getPrice());
		}).toList();
	}

	private int getIssuedShares(MarketResponseDTO body) {
		return (int)(body.getCap() / body.getPrice());
	}
}
