package com.bjcareer.search.application.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

public class ThemaCalculatorHelper {
	public static Map<ThemaInfo, List<Stock>> groupStocksUsingThema(List<Thema> themas) {
		Map<ThemaInfo, List<Stock>> groupingThema = new HashMap<>();

		themas.forEach(thema -> {
			ThemaInfo themaInfo = thema.getThemaInfo();
			Stock stock = thema.getStock();

			if (groupingThema.containsKey(themaInfo)) {
				groupingThema.get(themaInfo).add(stock);
			} else {
				groupingThema.put(themaInfo, new ArrayList<>(List.of(stock)));
			}
		});

		return groupingThema;
	}
}
