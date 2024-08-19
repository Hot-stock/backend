package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@SpringBootTest
class RedisTimeDealRepositoryTest {
	@Autowired
    private RedisTimeDealRepository redisTimeDealRepository;


	@Test
	void save_ShouldSaveEventAndReturnId() {
		Event eventMock = mock(Event.class);

		//when
		when(eventMock.getId()).thenReturn(1L);
		Long save = redisTimeDealRepository.save(eventMock, 3L);

		assertEquals(1L, save);
	}

	@Test
	void 찾기_실패_했을_때() {
		Event eventMock = mock(Event.class);
		when(eventMock.getId()).thenReturn(1L);
		Optional<Event> found = redisTimeDealRepository.findById(2L);
		assertEquals(Optional.empty(), found);
	}


	@Test
	void 찾기_성공_했을_때() {
		Event eventMock = mock(Event.class);
		when(eventMock.getId()).thenReturn(1L);

		Long save = redisTimeDealRepository.save(eventMock, 3L);
		Optional<Event> found = redisTimeDealRepository.findById(save);
		assertNotNull(found.get());
	}
}