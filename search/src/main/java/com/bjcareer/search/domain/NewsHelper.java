package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public final class NewsHelper {
	public static List<GPTStockNewsDomain> RemoveDuplicatedNews(List<GPTStockNewsDomain> news) {
		Map<LocalDate, List<GPTStockNewsDomain>> newsByDate = new HashMap<>();

		for (GPTStockNewsDomain gptStockNewsDomain : news) {
			if (!newsByDate.containsKey(gptStockNewsDomain.getNews().getPubDate())) {
				newsByDate.put(gptStockNewsDomain.getNews().getPubDate(), new ArrayList<>());
			}
			List<GPTStockNewsDomain> gptStockNewsDomains = newsByDate.get(gptStockNewsDomain.getNews().getPubDate());

			boolean duplicated = isDuplicated(gptStockNewsDomains, gptStockNewsDomain);

			if (!duplicated) {
				newsByDate.get(gptStockNewsDomain.getNews().getPubDate()).add(gptStockNewsDomain);
			}
		}

		List<GPTStockNewsDomain> result = new ArrayList<>();
		List<LocalDate> sortedPubDate = newsByDate.keySet().stream().sorted().toList().reversed();

		for (LocalDate key : sortedPubDate) {
			result.addAll(newsByDate.get(key));
		}

		return result;
	}

	private static boolean isDuplicated(List<GPTStockNewsDomain> source, GPTStockNewsDomain target) {
		for (GPTStockNewsDomain newsDomain : source) {
			boolean result = !Collections.disjoint(newsDomain.getKeywords(), target.getKeywords());

			if (result) {
				return true;
			}
		}
		return false;
	}
}
