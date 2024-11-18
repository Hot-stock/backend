package com.bjcareer.search.out.api.python;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.application.port.out.api.LoadNewsPort;
import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.application.port.out.api.StockChartQueryCommand;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Market;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.out.api.dto.NewsResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PythonSearchServerAdapter implements LoadStockInformationPort, LoadNewsPort {

	@Value("${python-search.address}")
	private String address;

	private final RestTemplate restTemplate;

	public ParseNewsContentResponseDTO getNewsBody(String link) {
		String encodedLink = URLEncoder.encode(link, StandardCharsets.UTF_8);
		String url = address + PythonServerURI.PARSE_CONTENT + encodedLink;
		log.debug("Requesting news body for link: {}", url);

		Optional<ParseNewsContentResponseDTO> responseDTO = fetchFromServer(url, HttpMethod.GET,
			new ParameterizedTypeReference<>() {
			});
		return responseDTO.orElseGet(ParseNewsContentResponseDTO::new);

	}

	@Override
	public StockChart loadStockChart(StockChartQueryCommand config) {
		String url = config.buildUrl(address + PythonServerURI.OHLC);

		return fetchFromServer(url, HttpMethod.GET, new ParameterizedTypeReference<List<OhlcResponseDTO>>() {
		})
			.map(response -> response.stream()
				.map(dto -> new OHLC(dto.getOpen(), dto.getHigh(), dto.getLow(), dto.getClose(),
					dto.getPercentageIncrease(), dto.getVolume(), dto.getDate()))
				.collect(Collectors.toList()))
			.map(ohlcs -> new StockChart(config.getStock().getCode(), ohlcs)).get();
	}

	@Override
	public List<Stock> loadStockInfo(Market market) {
		String url = address + PythonServerURI.MARKET + "?q=" + market.name();
		String hrefTemplate = "https://finance.naver.com/item/main.naver?code=";

		return fetchFromServer(url, HttpMethod.GET, new ParameterizedTypeReference<List<MarketResponseDTO>>() {
		})
			.orElse(List.of())
			.stream()
			.map(dto -> new Stock(
				dto.getSymbol(),
				dto.getName(),
				Market.fromString(dto.getMarket()),
				hrefTemplate + dto.getSymbol(),
				getIssuedShares(dto),
				dto.getPrice()
			))
			.collect(Collectors.toList());
	}

	@Override
	public List<News> fetchNews(NewsCommand command) {
		List<News> newsList = new ArrayList<>();
		String url = command.buildUrl(address + PythonServerURI.NEWS);
		log.debug("Requesting news for url: {}", url);

		Optional<List<NewsResponseDTO>> newsResponseDTOS = fetchFromServer(url, HttpMethod.GET,
			new ParameterizedTypeReference<List<NewsResponseDTO>>() {
			});

		if (newsResponseDTOS.isEmpty()) {
			log.debug("No news found for command: {}", command);
			return List.of();
		}

		List<NewsResponseDTO> newsResponseDTO = newsResponseDTOS.get();

		for (NewsResponseDTO dto : newsResponseDTO) {
			ParseNewsContentResponseDTO content = getNewsBody(dto.getLink());
			log.debug("Fetched news body for link: {}", dto.getLink());

			if (content.getPublishDate().isEmpty() || content.getText().isEmpty()) {
				continue;
			}

			newsList.add(new News("", dto.getLink(), dto.getLink(), "", content.getPublishDate(), content.getText()));
		}

		return newsList;
	}

	@Override
	public List<Stock> loadRanking(Market market) {
		List<Stock> newsList = new ArrayList<>();
		String url = address + PythonServerURI.RANKING + "?q=" + market.name();
		log.debug("Requesting news for url: {}", url);

		return fetchFromServer(url, HttpMethod.GET, new ParameterizedTypeReference<List<MarketResponseDTO>>() {
		})
			.orElse(List.of())
			.stream()
			.map(dto -> new Stock(
				dto.getSymbol(),
				dto.getName()
			))
			.collect(Collectors.toList());
	}

	private int getIssuedShares(MarketResponseDTO dto) {
		return (int)(dto.getCap() / dto.getPrice());
	}

	// 공통 REST 호출 메서드로 재사용성 향상
	private <T> Optional<T> fetchFromServer(String url, HttpMethod method, ParameterizedTypeReference<T> responseType) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(url, method, null, responseType);
			if (response.getStatusCode().is2xxSuccessful()) {
				return Optional.ofNullable(response.getBody());
			} else {
				log.error("Failed to fetch from server. URL: {}, Status Code: {}", url, response.getStatusCode());
			}
		} catch (RestClientException e) {
			log.error("Exception while fetching data from server. URL: {}, Error: {}", url, e.getMessage());
		}
		return Optional.empty();
	}
}
