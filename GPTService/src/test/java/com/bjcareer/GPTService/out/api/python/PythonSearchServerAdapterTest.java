package com.bjcareer.GPTService.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.port.out.api.NewsCommand;
import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;

@SpringBootTest
class PythonSearchServerAdapterTest {
	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 지정된_뉴스를_패치할_수_있는지_검증() {
		NewsCommand newsCommand = new NewsCommand("이준석", LocalDate.of(2024, 12, 17), LocalDate.of(2024, 12, 17));
		List<NewsResponseDTO> newsResponseDTOS = pythonSearchServerAdapter.fetchNews(newsCommand);
		assertNotNull(newsResponseDTOS);
		System.out.println("originalNews = " + newsResponseDTOS);
	}

}
