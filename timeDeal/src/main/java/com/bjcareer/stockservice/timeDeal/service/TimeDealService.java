package com.bjcareer.stockservice.timeDeal.service;

import java.util.List;
import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RScoredSortedSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    public static final String QUEUE_NAME = "EVENT:QUEUE:";
    private static final long ALIVE_MINUTE = 5L;

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final Redis redis;

    @Transactional
    public Event createEvent(int publishedCouponNum, int discountRate) {
        Event event = new Event(publishedCouponNum, discountRate);
        return eventRepository.save(event);
    }

    public int addParticipation(Long eventId, String userPK) {
        long currentTimeMillis = System.currentTimeMillis();
        RScoredSortedSet<String> participationQueue = redis.getScoredSortedSet(TimeDealService.QUEUE_NAME + eventId);
        participationQueue.add(-currentTimeMillis, userPK);
        return participationQueue.size();
    }

    @Transactional
    public int updateEventStatus(Long eventId, int participationsSize) {
        String lockKey = "EVENT:LOCK" + eventId;

        boolean isLockAcquired = redis.acquireLock(lockKey);

        if (!isLockAcquired) {
            throw new IllegalStateException("Unable to acquire lock for event update.");
        }

        try {
            Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new InvalidEventException("Event not found for id: " + eventId));
            event.validateEventStatus();

            int excessParticipants = event.deliverCoupons(participationsSize);
            inMemoryEventRepository.save(event, ALIVE_MINUTE);
            eventRepository.save(event);

            return excessParticipants;
        } finally {
            redis.releaseLock(lockKey);
        }
    }

    public void bulkGenerateCoupon(Long eventId, List<String> clients) {
        Event event = inMemoryEventRepository.findById(eventId)
            .orElseThrow(() -> new InvalidEventException("Event not found in in-memory storage for id: " + eventId));


        clients.forEach(client -> {
            Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId, client);

            if (byEventIdAndUserId.isPresent()) {
                return;
            }

            Coupon coupon = new Coupon(event, client);
            couponRepository.saveAsync(coupon);
        });
    }
}
