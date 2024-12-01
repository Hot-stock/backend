package com.bjcareer.GPTService.out.api.toss;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TossServerAdapter {
	private final String SOAR_STOCK_URI = "https://wts-cert-api.tossinvest.com/api/v2/dashboard/wts/overview/ranking";
	private final WebClient webClient;

	public SoarStockResponseDTO getSoarStock() {
		SoarStockRequestDTO soarStockRequestDTO = new SoarStockRequestDTO();
		return webClient.post()
			.uri(SOAR_STOCK_URI) // POST 요청 대상 엔드포인트
			.bodyValue(soarStockRequestDTO) // 요청 Body
			.retrieve()
			.bodyToMono(SoarStockResponseDTO.class) // 응답 Body를 String으로 변환
			.block(); // 동기 방식으로 결과 반환
	}

}
