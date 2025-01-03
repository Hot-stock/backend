package com.bjcareer.search.out.persistence.noSQL;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

@SpringBootTest
class DocumentAnalyzeThemaRepositoryTest {
	@Autowired
	DocumentAnalyzeThemaRepository documentAnalyzeThemaRepository;

	@Test
	void 테마이름으로_된_뉴스를_찾아오는지() {
		List<GPTThemaNewsDomain> result = documentAnalyzeThemaRepository.getThemaNews("중국 경기 부양책");
		assertEquals(4, result.size());
	}
}
