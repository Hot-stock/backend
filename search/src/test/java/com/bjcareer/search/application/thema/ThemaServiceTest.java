package com.bjcareer.search.application.thema;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

@SpringBootTest
class ThemaServiceTest {
	@Autowired
	ThemaService themaService;

	@Test
	void 저장된_테마가_로드되는지_확인() {
		LoadThemaNewsCommand command = new LoadThemaNewsCommand(156, 1, 2);
		PaginationDomain<GPTThemaNewsDomain> gptThemaNewsDomainPaginationDomain = themaService.loadThemaNewsByQuery(
			command);

		Assertions.assertEquals(1, gptThemaNewsDomainPaginationDomain.getCurrentPage());
	}
}
