package com.bjcareer.search.out.api.toss;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.out.api.toss.dtos.CandleResponseDTO;
import com.bjcareer.search.out.api.toss.dtos.TossStockInfoDTO;

@SpringBootTest
class TossServerAdapterTest {

	@Autowired
	private TossServerAdapter tossServerAdapter;

	@Test
	void 토스_주식_ohlc차트_갱신_api(){
		CandleResponseDTO responseDTO = tossServerAdapter.getStockPriceURI("041020", "day");
		System.out.println("stockPriceURI = " + responseDTO);

		System.out.println("sto = " + responseDTO.getResult());
	}

	@Test
	void 토스_주식_정보(){
		TossStockInfoDTO stockInfo = tossServerAdapter.getStockInfo("041020");
		System.out.println("stockPriceURI = " + stockInfo.getResult().getLogoImageUrl());
	}

}
