package com.bjcareer.search.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PythonSearchServerAdapterTest {

	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void ohlc_요청_테스트() {
		OHLCQueryConfig config = new OHLCQueryConfig("003780", true);
		List<OhlcResponseDTO> stockOHLC = pythonSearchServerAdapter.getStockOHLC(config);

		System.out.println("stockOHLC = " + stockOHLC);
	}
}
