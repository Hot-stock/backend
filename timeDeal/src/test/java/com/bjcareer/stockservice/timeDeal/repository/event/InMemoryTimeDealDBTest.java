package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class InMemoryTimeDealDBTest {
    @Autowired
    private RedissonClient redissonClient;

    @Mock
    private InMemoryEventRepository inMemoryTimeDealRepository;
    @Mock
    private EventRepository eventRepository;



    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        inMemoryTimeDealRepository = mock(InMemoryEventRepository.class);
    }

    @Test
    void event_티켓_저장(){
        //given
        Long saveId = 1L;
        TimeDealEvent event = mock(TimeDealEvent.class);
        given(event.getId()).willReturn(saveId);

        //when
        RMap<Long, TimeDealEvent> test = redissonClient.getMap("TimeDealEvet" + saveId.toString());
        test.put(event.getId(), event);
        TimeDealEvent event1 = test.get(event.getId());

        assertEquals(event.getPublishedCouponNum(), event1.getPublishedCouponNum());
    }

    @Test
    void event가_저장되는지_테스트(){
        Long saveId = 1L;
        TimeDealEvent event = mock(TimeDealEvent.class);
        given(inMemoryTimeDealRepository.save(event)).willReturn(saveId);

        Long save = inMemoryTimeDealRepository.save(event);
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket("timeDeal:" + save.toString());
        assertEquals(1, bucket.get().getId());
    }
}