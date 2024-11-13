package com.bjcareer.search.application.port.out.api;

import java.time.LocalDate;
import java.util.List;

import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

public interface GPTInsightPort {
	GPTInsight getInsight(List<GTPNewsDomain> newes, List<GPTThema> themas, LocalDate baseDate);
}
