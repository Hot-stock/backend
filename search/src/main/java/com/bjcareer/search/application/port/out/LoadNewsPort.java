package com.bjcareer.search.application.port.out;

import java.util.List;

import com.bjcareer.search.domain.News;
import com.bjcareer.search.out.api.naver.NaverNewsQueryConfig;

public interface LoadNewsPort {
	List<News> fetchNews(NaverNewsQueryConfig config);
}
