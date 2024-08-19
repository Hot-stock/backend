package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.RedisLock;
import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.CouponLimitExceededException;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class TimeDealServiceTest {
    public static final int PUBLISHED_COUPON_NUM = 100;
    public static final int DISCOUNT_PERCENTAGE = 50;
    TimeDealService timeDealService;
    EventRepository eventRepository;
    CouponRepository couponRepository;
    InMemoryEventRepository inMemoryEventRepository;

    @Autowired RedisLock redisLock;

    @BeforeEach
    void setUp() {
        couponRepository = Mockito.mock(CouponRepository.class);
        eventRepository = Mockito.mock(EventRepository.class);
        inMemoryEventRepository = Mockito.mock(InMemoryEventRepository.class);

        timeDealService = new TimeDealService(couponRepository, eventRepository, inMemoryEventRepository, redisLock);
    }

    @Test
    void createEvent_ShouldReturnSavedEvent() {
        // When
        Event event = new Event(TimeDealServiceTest.PUBLISHED_COUPON_NUM, TimeDealServiceTest.DISCOUNT_PERCENTAGE);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // given
        Event result = timeDealService.createEvent(PUBLISHED_COUPON_NUM, DISCOUNT_PERCENTAGE);

        // Then
        assertNotNull(result);
        assertEquals(PUBLISHED_COUPON_NUM, result.getPublishedCouponNum());
        assertEquals(DISCOUNT_PERCENTAGE, result.getDiscountPercentage());

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void 잘못된_이벤트Id로_쿠폰_발급을_요청하는_경우(){
        Long InvaildEventId = -1L;
        String userId = "testuser";
        assertThrows(InvalidEventException.class , () -> timeDealService.generateCouponToUser(InvaildEventId, userId));
    }

    @Test
    void 티켓을_더이상_발급할_수_없는_상황일(){
        Event eventMock = mock(Event.class);

        when(eventMock.getId()).thenReturn(1L);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventMock));

        assertThrows(CouponLimitExceededException.class , () -> timeDealService.generateCouponToUser(eventMock.getId(), "testUser"));
    }

    @Test
    void 발급한_수보다_더_많은_사용자가_요청함() throws InterruptedException {
        int TOTAL_REQUESTS = 4;
        int TOTAL_COUPONS = 2;
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);
        ExecutorService executorService = Executors.newFixedThreadPool(TOTAL_REQUESTS);

        Event eventMock = new Event(TOTAL_COUPONS, DISCOUNT_PERCENTAGE);
        when(inMemoryEventRepository.findById(any())).thenReturn(Optional.of(eventMock));

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            final String userId = "testuser" + i;
            executorService.submit(() -> {
                try {
                    Coupon coupon = timeDealService.generateCouponToUser(eventMock.getId(), userId);
                    assertNotNull(coupon);
                } catch (CouponLimitExceededException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 종료될 때까지 대기
        executorService.shutdown();
        assertEquals(eventMock.getDeliveredCouponNum(), 2);
        assertEquals(failCount.get(), TOTAL_REQUESTS - TOTAL_COUPONS);
    }

}