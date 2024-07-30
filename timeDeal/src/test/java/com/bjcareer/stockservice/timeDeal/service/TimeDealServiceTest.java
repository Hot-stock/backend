package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.RedisLock;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

class TimeDealServiceTest {
    private TimeDealService timeDealService;
    @Mock private CouponRepository couponRepository;
    @Mock private RedisLock redisLock;
    @Mock private EventRepository eventRepository;
    @Mock private InMemoryEventRepository inMemoryEventRepository;

    private String LOCK_KEY = "TIME_DEAL_LOCK";

    @BeforeEach
    void setUp() {
        redisLock = mock(RedisLock.class);
        eventRepository = mock(EventRepository.class);
        inMemoryEventRepository = mock(InMemoryEventRepository.class);
        couponRepository = mock(CouponRepository.class);

        timeDealService = new TimeDealService(couponRepository, eventRepository, inMemoryEventRepository, redisLock);
    }

    @Test
    void testCreateTimeDealEvent_lockAcquired() {
        int publishedCouponNum = 100;

        given(redisLock.tryLock(LOCK_KEY)).willReturn(true);
        given(eventRepository.save(any(TimeDealEvent.class))).willReturn(1L);
        given(inMemoryEventRepository.save(any(TimeDealEvent.class))).willReturn(1L);

        // when
        Optional<TimeDealEvent> result = timeDealService.createTimeDealEvent(publishedCouponNum);

        // then
        assertTrue(result.isPresent(), "The event should be created successfully.");
        assertEquals(publishedCouponNum, result.get().getPublishedCouponNum(), "Published coupon number should match.");
    }

    @Test
    void testCreateTimeDealEvent_lockNotAcquired() {
        int publishedCouponNum = 100;

        given(redisLock.tryLock(LOCK_KEY)).willReturn(false);

        // when
        Optional<TimeDealEvent> result = timeDealService.createTimeDealEvent(publishedCouponNum);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    void testGenerateCouponToUser_withInvalidEventId() {
        // given
        Long EVENT_ID = 99L;

        given(inMemoryEventRepository.findById(99L)).willReturn(null);

        // then
        assertThrows(IllegalStateException.class , () -> timeDealService.generateCouponToUser(-99L, 20.0));
    }

    @Test
    void testGenerateCouponToUser_whenNoMoreTicketsAvailable() {
        // given
        Long EVENT_ID = 1L;

        given(inMemoryEventRepository.findById(EVENT_ID)).willReturn(new TimeDealEvent(0));
        given(redisLock.tryLock(LOCK_KEY)).willReturn(true);

        // then
        assertFalse(timeDealService.generateCouponToUser(EVENT_ID, 20.0).isPresent());
        verify(eventRepository, times(1)).saveAsync(any(TimeDealEvent.class));
    }

    @Test
    void testGenerateCouponToUser_whenMoreRequestsThanCouponsAvailable() throws InterruptedException {
        // setup
        Long EVENT_ID = 1L;
        TimeDealEvent timeDealEvent = new TimeDealEvent(1);

        // given
        given(inMemoryEventRepository.findById(EVENT_ID)).willReturn(timeDealEvent);
        given(redisLock.tryLock(LOCK_KEY)).willReturn(true);

        // then
        Optional<Coupon> publishedCoupon = timeDealService.generateCouponToUser(EVENT_ID, 20.0);
        Optional<Coupon> cantGetCoupon = timeDealService.generateCouponToUser(EVENT_ID, 20.0);

        assertTrue(publishedCoupon.isPresent());
        assertFalse(cantGetCoupon.isPresent());

        verify(eventRepository, times(1)).saveAsync(any(TimeDealEvent.class));
    }

    @Test
    void testGenerateCouponToUser_whenAcquireFailOfRedisLock() throws InterruptedException {
        // setup
        Long EVENT_ID = 1L;
        TimeDealEvent timeDealEvent = new TimeDealEvent(1);

        // given
        given(inMemoryEventRepository.findById(EVENT_ID)).willReturn(timeDealEvent);
        given(redisLock.tryLock(LOCK_KEY)).willReturn(true).willReturn(false);

        // then
        Optional<Coupon> publishedCoupon = timeDealService.generateCouponToUser(EVENT_ID, 20.0);
        assertTrue(publishedCoupon.isPresent());
        verify(inMemoryEventRepository, times(1)).save(any(TimeDealEvent.class));

        assertThrows(RedisLockAcquisitionException.class, () -> timeDealService.generateCouponToUser(EVENT_ID, 20.0));
    }
}
