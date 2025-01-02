// package com.bjcareer.search.application.helper;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.jupiter.api.Test;
//
// import com.bjcareer.search.domain.entity.Stock;
// import com.bjcareer.search.domain.entity.Thema;
// import com.bjcareer.search.domain.entity.ThemaInfo;
//
// class ThemaCalculatorHelperTest {
//
// 	@Test
// 	void testGroupStocksUsingThema() {
// 		// given
// 		Thema thema1 = new Thema(new Stock("1234", "배럴"), new ThemaInfo("수영복"));
// 		Thema thema2 = new Thema(new Stock("1232", "효성"), new ThemaInfo("수영복"));
// 		Thema thema3 = new Thema(new Stock("1232", "효성"), new ThemaInfo("2차전지"));
//
// 		List<Thema> themas = new ArrayList<>();
// 		themas.add(thema1);
// 		themas.add(thema2);
// 		themas.add(thema3);
//
// 		// when
// 		Map<ThemaInfo, List<Stock>> result = ThemaCalculatorHelper.groupStocksUsingThema(themas);
// 		System.out.println("result = " + result);
// 		// then
// 		assertEquals(2, result.size(), "수영복과 2차전지 두개의 테마가 있어야 한다.");
// 		assertEquals(2, result.get(new ThemaInfo("수영복")).size(), "수영복 테마에는 2개의 주식이 있어야 한다.");
// 		assertEquals(1, result.get(new ThemaInfo("2차전지")).size(), "2차전지 테마에는 1개의 주식이 있어야 한다.");
//
// 	}
//
// 	// public static Map<ThemaInfo, List<Stock>> groupStocksUsingThema(List<Thema> themas) {
// 	// 	Map<ThemaInfo, List<Stock>> groupingThema = new HashMap<>();
// 	//
// 	// 	themas.stream().forEach(thema -> {
// 	// 		ThemaInfo themaInfo = thema.getThemaInfo();
// 	// 		Stock stock = thema.getStock();
// 	//
// 	// 		if (groupingThema.containsKey(themaInfo)) {
// 	// 			groupingThema.get(themaInfo).add(stock);
// 	// 		} else {
// 	// 			groupingThema.put(themaInfo, List.of(stock));
// 	// 		}
// 	// 	});
// 	//
// 	// 	return groupingThema;
// 	// }
// }
