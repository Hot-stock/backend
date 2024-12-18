package com.bjcareer.search.application.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;

@SpringBootTest
class SearchServiceTest {

	@Autowired
	SearchService searchService;

	@Test
	void test_filterThemesByQuery() {
		List<GPTStockNewsDomain> raiseReason = searchService.findRaiseReason("065500", LocalDate.of(2024, 12, 13));
		assertNotNull(raiseReason);
	}
}
