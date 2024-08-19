package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.RedisLock;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.DuplicateParticipationException;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    private static final String LOCK_KEY_PREFIX = "EVENT_LOCK:";
    private static final long ALIVE_MINUTE = 5L;

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final RedisLock redisLock;

    @Transactional
    public Event createEvent(int publishedCouponNum, int discountRate) {
        Event event = new Event(publishedCouponNum, discountRate);
        return eventRepository.save(event);
    }

    @Transactional
    public Coupon generateCouponToUser(Long eventId, String userPK) {
        String lockKey = LOCK_KEY_PREFIX + eventId;
        redisLock.tryLock(lockKey);

        try {
            Event event = loadEventToMemoryIfNotExists(eventId);
            validateEvent(eventId, event);
            validateDuplicateParticipation(event, userPK);
            return createCoupon(event, userPK);
        } finally {
            redisLock.releaselock(lockKey);
        }
    }

    private void validateEvent(Long eventId, Event event) {
        try {
            event.checkEventStatus();
            event.incrementDeliveredCouponIfPossible();
        } catch (Exception e) {
            throw new InvalidEventException("Invalid event state: " + eventId);
        }
    }

    private void validateDuplicateParticipation(Event event, String userPK) {
        boolean isDuplicate = inMemoryEventRepository.findParticipant(event, userPK).isPresent() ||
            couponRepository.findByEventIdAndUserId(event.getId(), userPK).isPresent();

        if (isDuplicate) {
            throw new DuplicateParticipationException("User has already participated in this event.");
        }
    }

    private Event loadEventToMemoryIfNotExists(Long eventId) {
        return inMemoryEventRepository.findById(eventId)
            .orElseGet(() -> loadEventDataToMemory(eventId));
    }

    private Event loadEventDataToMemory(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new InvalidEventException("Invalid event ID provided: eventId " + eventId));
        inMemoryEventRepository.save(event, ALIVE_MINUTE);
        return event;
    }

    private Coupon createCoupon(Event event, String userPK) {
        Coupon coupon = new Coupon(event, userPK);
        couponRepository.save(coupon);
        inMemoryEventRepository.saveClient(event, ALIVE_MINUTE, userPK);
        return coupon;
    }
}
