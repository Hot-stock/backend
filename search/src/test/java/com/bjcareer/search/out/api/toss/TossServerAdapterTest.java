package com.bjcareer.search.out.api.toss;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.out.api.toss.dtos.CandleResponseDTO;

@SpringBootTest
class TossServerAdapterTest {

	@Autowired
	private TossServerAdapter tossServerAdapter;

	@Test
	void test(){
		CandleResponseDTO responseDTO = tossServerAdapter.getStockPriceURI("041020");
		System.out.println("stockPriceURI = " + responseDTO);

		System.out.println("sto = " + responseDTO.getResult());


	}

}
