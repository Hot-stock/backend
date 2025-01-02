// package com.bjcareer.GPTService.application;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
//
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import com.bjcareer.GPTService.TestUtil;
// import com.bjcareer.GPTService.application.port.in.AnalyzeStockNewsCommand;
// import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
// import com.bjcareer.GPTService.domain.gpt.OriginalNews;
// import com.bjcareer.GPTService.out.api.dto.NewsResponseDTO;
// import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
// import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
// import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
//
// @ExtendWith(MockitoExtension.class) // MockitoExtension 사용
// class GPTStockNewsServiceTest {
// 	@Mock
// 	GPTStockNewsRepository gptStockNewsRepository;
//
// 	@Mock
// 	PythonSearchServerAdapter pythonSearchServerAdapter;
//
// 	@Mock
// 	GPTNewsAdapter gptNewsAdapter;
//
// 	@InjectMocks
// 	GPTStockAnalyzeService gptStockNewsService;
//
// 	@Test
// 	public void 저장된_뉴스들을_저장할_수_있어야_함() {
// 		AnalyzeStockNewsCommand command = new AnalyzeStockNewsCommand("www.naver.com");
// 		OriginalNews originalNews = new OriginalNews("배럴", "fakeLink", "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");
// 		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", "2021-07-01", "더위가 심해지면서",
// 			"더위가 심해진다", originalNews, true, "", true, new ArrayList<>());
// 		when(gptStockNewsRepository.findByLink(anyString())).thenReturn(Optional.of(gptNewsDomain));
//
// 		GPTNewsDomain gptStockNews = gptStockNewsService.analyzeStockNewsByNewsLink(command);
// 		assertEquals(gptNewsDomain, gptStockNews);
// 	}
//
// 	@Test
// 	public void 저장된_뉴스_분석요청_save함수_호출_안됨() {
// 		String stockName = "배럴";
// 		LocalDate date = LocalDate.now();
// 		List<NewsResponseDTO> result = new ArrayList<>();
//
// 		result.add(new NewsResponseDTO("www.naver.com"));
//
// 		OriginalNews originalNews = new OriginalNews("배럴", "fakeLink", "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");
// 		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", "2021-07-01", "더위가 심해지면서",
// 			"더위가 심해진다", originalNews, true, "", true, new ArrayList<>());
//
// 		when(pythonSearchServerAdapter.fetchNews(any())).thenReturn(result);
// 		when(gptStockNewsRepository.findByLink(result.get(0).getLink())).thenReturn(Optional.of(gptNewsDomain));
//
// 		List<GPTNewsDomain> gptNewsDomains = gptStockNewsService.analyzeStockNewsByDateWithStockName(date, stockName);
//
// 		verify(gptStockNewsRepository, times(0)).save(any());
// 	}
// }
