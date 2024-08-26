package com.bjcareer.stockservice.timeDeal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RedissonClient;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

public class TimeDealServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private InMemoryEventRepository inMemoryEventRepository;

    @Mock
    private Redis redis;

    @Mock
    private RedisQueue redisQueue;

    @InjectMocks
    private TimeDealService timeDealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        timeDealService = new TimeDealService(couponRepository, eventRepository, inMemoryEventRepository, redisQueue, redis);
    }

    @Test
    void createEvent_ShouldSaveEvent() {
        // Arrange
        int publishedCouponNum = 100;
        int discountRate = 20;

        Event event = new Event(publishedCouponNum, discountRate);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event createdEvent = timeDealService.createEvent(publishedCouponNum, discountRate);

        // Assert
        assertNotNull(createdEvent);
        assertEquals(publishedCouponNum, createdEvent.getPublishedCouponNum());
        assertEquals(discountRate, createdEvent.getDiscountPercentage());
    }

    @Test
    void updateEventStatus_ShouldUpdateEventAndReturnExcessParticipants() {
        // Arrange
        Long eventId = 1L;
        int participationsSize = 50;
        Event event = mock(Event.class);

        when(redis.acquireLock(anyString())).thenReturn(true);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(event.deliverCoupons(anyInt())).thenReturn(10);

        // Act
        int excessParticipants = timeDealService.updateDeliveryEventCoupon(eventId, participationsSize);

        // Assert
        assertEquals(10, excessParticipants);
    }

    @Test
    void bulkGenerateCoupon_ShouldGenerateCouponsForClients() {
        // Arrange
        Long eventId = 1L;
        List<String> clients = Arrays.asList("client1", "client2");
        Event event = mock(Event.class);

        when(inMemoryEventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        // Act
        timeDealService.bulkGenerateCoupon(eventId, clients);

        // Assert
        verify(couponRepository, times(clients.size())).saveAsync(any(Coupon.class));
    }

    @Test
    void updateEventStatus_ShouldThrowException_WhenLockNotAcquired() {
        // Arrange
        Long eventId = 1L;
        int participationsSize = 50;

        when(redis.acquireLock(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> timeDealService.updateDeliveryEventCoupon(eventId, participationsSize));
    }

    @Test
    void updateEventStatus_ShouldThrowException_WhenEventNotFound() {
        // Arrange
        Long eventId = 1L;
        int participationsSize = 50;

        when(redis.acquireLock(anyString())).thenReturn(true);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidEventException.class, () -> timeDealService.updateDeliveryEventCoupon(eventId, participationsSize));

        verify(redis, times(1)).releaseLock(anyString());
    }
}
