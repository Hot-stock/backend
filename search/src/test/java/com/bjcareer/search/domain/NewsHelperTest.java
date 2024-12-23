package com.bjcareer.search.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

class NewsHelperTest {

	@Test
	void 중복_뉴스_제거_테스트() {
		GPTStockNewsDomain newsDomain1 = new GPTStockNewsDomain("써니전자", "07340", "안철수 대선",
			new ArrayList<>(Arrays.asList("안철수")), "2021-07-01", "대선이 오른다");
		GPTStockNewsDomain newsDomain2 = new GPTStockNewsDomain("써니전자", "07340", "안철수 대선",
			new ArrayList<>(Arrays.asList("이재명")), "2021-07-01", "대선이 오른다");
		GPTStockNewsDomain newsDomain3 = new GPTStockNewsDomain("써니전자", "07340", "안철수 대선",
			new ArrayList<>(Arrays.asList("안철수", "정치테마주")), "2021-07-01", "대선이 오른다");

		ArrayList<GPTStockNewsDomain> domains = new ArrayList<>();
		domains.add(newsDomain1);
		domains.add(newsDomain2);
		domains.add(newsDomain3);

		List<GPTStockNewsDomain> gptStockNewsDomains = NewsHelper.RemoveDuplicatedNews(domains);

		assertEquals(2, gptStockNewsDomains.size());
	}

}
