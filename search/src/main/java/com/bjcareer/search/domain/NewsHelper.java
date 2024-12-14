package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;

public final class NewsHelper {
	public static List<GPTNewsDomain> RemoveDuplicatedNews(List<GPTNewsDomain> news) {
		Map<LocalDate, List<GPTNewsDomain>> newsByDate = new HashMap<>();

		for (GPTNewsDomain gptNewsDomain : news) {
			if (!newsByDate.containsKey(gptNewsDomain.getNews().getPubDate())) {
				newsByDate.put(gptNewsDomain.getNews().getPubDate(), new ArrayList<>());
			}
			List<GPTNewsDomain> gptNewsDomains = newsByDate.get(gptNewsDomain.getNews().getPubDate());

			boolean duplicated = isDuplicated(gptNewsDomains, gptNewsDomain);

			if (!duplicated) {
				newsByDate.get(gptNewsDomain.getNews().getPubDate()).add(gptNewsDomain);
			}
		}

		List<GPTNewsDomain> result = new ArrayList<>();
		List<LocalDate> sortedPubDate = newsByDate.keySet().stream().sorted().toList().reversed();

		for (LocalDate key : sortedPubDate) {
			result.addAll(newsByDate.get(key));
		}

		return result;
	}

	private static boolean isDuplicated(List<GPTNewsDomain> source, GPTNewsDomain target) {
		for (GPTNewsDomain newsDomain : source) {
			boolean result = !Collections.disjoint(newsDomain.getKeywords(), target.getKeywords());

			if (result) {
				return true;
			}
		}
		return false;
	}
}
