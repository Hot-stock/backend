package com.bjcareer.GPTService.domain.gpt.thema;

import java.util.List;

import com.bjcareer.GPTService.domain.helper.LevenshteinDistance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ThemaInfo {
	private List<String> stockName;
	private String name;
	private String reason;

	public ThemaInfo(List<String> stockName, String name, String reason) {
		this.stockName = stockName;
		this.name = name;
		this.reason = reason;
	}

	public void changeThemaNameUsingLevenshteinDistance(List<String> themas) {
		boolean contains = themas.contains(this.name);
		if (contains) {
			return;
		}
		for (String thema : themas) {
			if (LevenshteinDistance.calculateLevenshteinDistance(this.name, thema) <= 3) {
				this.name = thema;
				return;
			}
		}
	}
}
