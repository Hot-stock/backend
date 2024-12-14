package com.bjcareer.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.CommonConfig;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

class NewsHelperTest {

	@Test
	void test() {
		String pubDate = CommonConfig.createPubDate();

		News news = new News("안철수", "link", "link", "desc", pubDate, "content");
		News news1 = new News("이재명", "link", "link", "desc", pubDate, "content");
		News news2 = new News("정치테마주", "link", "link", "desc", pubDate, "content");

		GPTNewsDomain newsDomain1 = new GPTNewsDomain("써니전자", "이사",
			new ArrayList<>(Arrays.asList("안철수")), "2021-07-01",
			"다음 이사는 2021-07-01", news);
		GPTNewsDomain newsDomain2 = new GPTNewsDomain("에이텍", "이사",
			new ArrayList<>(Arrays.asList("이재명")), "2021-07-01",
			"다음 이사는 2021-07-01", news1);
		GPTNewsDomain newsDomain3 = new GPTNewsDomain("써니전자", "이사",
			new ArrayList<>(Arrays.asList("정치테마주", "안철수")), "2021-07-01",
			"다음 이사는 2021-07-01", news2);

		ArrayList<GPTNewsDomain> domains = new ArrayList<>();
		domains.add(newsDomain1);
		domains.add(newsDomain2);
		domains.add(newsDomain3);

		List<GPTNewsDomain> gptNewsDomains = NewsHelper.RemoveDuplicatedNews(domains);

		assertEquals(2, gptNewsDomains.size());
	}

}
