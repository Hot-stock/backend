package com.bjcareer.GPTService.out.persistence.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;

@SpringBootTest
class GPTThemaNewsRepositoryTest {
	@Autowired
	GPTThemaNewsRepository gptThemaNewsRepository;

	@Test
	void 몽고테스트(){
		List<GPTThema> t = gptThemaNewsRepository.findByName("김한길");
	}
}
