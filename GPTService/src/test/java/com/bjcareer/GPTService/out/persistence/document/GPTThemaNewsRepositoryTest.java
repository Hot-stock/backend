package com.bjcareer.GPTService.out.persistence.document;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;

@SpringBootTest
class GPTThemaNewsRepositoryTest {
	@Autowired
	GPTThemaNewsRepository gptThemaNewsRepository;

	@Test
	void GPTStockThema가_저장될_수_있는지() {
		OriginalNews news = new OriginalNews("", "", "", LocalDate.now().toString(), "");
		GPTStockThema stockThema = new GPTStockThema(")link", false,
			List.of(new ThemaInfo(List.of("에이텍", "동신건설"), "이재명", "ttt")));
		GPTStockThema save = gptThemaNewsRepository.save(stockThema);
		assertNotNull(save);
	}

	@Test
	void 저장된_테마를_찾을_수_있는지() {
		List<GPTStockThema> list = gptThemaNewsRepository.findByThemaName("이재명");
		assertFalse(list.isEmpty());
	}

	@Test
	void 주식_이름으로_테마를_찾을_수_있는지() {
		List<GPTStockThema> list = gptThemaNewsRepository.findByStockName("에이텍");
		assertFalse(list.isEmpty());
	}
}
