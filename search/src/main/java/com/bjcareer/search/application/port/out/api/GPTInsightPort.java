package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

public interface GPTInsightPort {
	GPTInsight getInsight(List<GPTStockNewsDomain> newes, List<GPTThemaNewsDomain> themas, LocalDate baseDate);
}
