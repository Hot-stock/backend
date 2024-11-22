package com.bjcareer.GPTService.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.in.GetGPTStockNewsCommand;
import com.bjcareer.GPTService.out.api.gpt.news.GPTNewsAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

@ExtendWith(MockitoExtension.class) // MockitoExtension 사용
class GPTStockNewsServiceTest {
	@Mock
	GPTStockNewsRepository gptStockNewsRepository;

	@Mock
	GPTNewsAdapter gptNewsAdapter;

	@InjectMocks
	GPTStockNewsService gptStockNewsService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);
	LocalDateTime now = LocalDateTime.now();
	String date = now.atZone(ZoneId.of("GMT")).format(formatter);

	@Test
	public void 저장되어_있지_않는_뉴스들을_저장할_수_있어야_함() {
		GetGPTStockNewsCommand command = new GetGPTStockNewsCommand("배럴", "더위에 배럴 상승", "배럴", "www.news.com", date);
		when(gptStockNewsRepository.findByLink(anyString())).thenReturn(Optional.empty());
		when(gptNewsAdapter.findStockRaiseReason(any(), any(), any())).thenReturn(
			Optional.of(new GPTNewsDomain("배럴", "더위", null, "2021-07-01", "더위가 심해지면서",
				new OriginalNews("배럴", "더위가 심해지면서", "www.news.com", "더위가 심해지면서", date, "더위가 심해지면서"))));

		gptStockNewsService.getGPTStockNews(command);

		verify(gptNewsAdapter).findStockRaiseReason(any(), any(), any());
		verify(gptStockNewsRepository).save(any());
	}

	@Test
	public void 저장된_뉴스들을_저장할_수_있어야_함() {
		GetGPTStockNewsCommand command = new GetGPTStockNewsCommand("배럴", "더위에 배럴 상승", "배럴", "www.news.com", date);

		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", null, "2021-07-01", "더위가 심해지면서",
			new OriginalNews("배럴", "더위가 심해지면서", "www.news.com", "더위가 심해지면서", date, "더위가 심해지면서"));

		when(gptStockNewsRepository.findByLink(anyString())).thenReturn(Optional.of(gptNewsDomain));

		GPTNewsDomain gptStockNews = gptStockNewsService.getGPTStockNews(command);
		assertEquals(gptNewsDomain, gptStockNews);
	}

}
