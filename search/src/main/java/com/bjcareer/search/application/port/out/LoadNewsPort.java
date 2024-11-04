package com.bjcareer.search.application.port.out;

import java.util.List;

import com.bjcareer.search.domain.News;

public interface LoadNewsPort {
	List<News> fetchNews(NewsCommand config);
}
