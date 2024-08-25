package com.bjcareer.stockservice.timeDeal.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    public static final String REDIS_QUEUE_NAME = "EVENT:QUEUE:";
    public static final String REDIS_PARTICIPANT_SET = "EVENT:PARTICIPANT:";

    private static final long ALIVE_MINUTE = 5L;

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final RedisQueue redisQueue;
    private final Redis redis;

    @Transactional
    public Event createEvent(int publishedCouponNum, int discountRate) {
        Event event = new Event(publishedCouponNum, discountRate);
        return eventRepository.save(event);
    }

    public int addParticipation(Long eventId, String clientPK) {
        String queueKey = TimeDealService.REDIS_QUEUE_NAME + eventId;
        String eventParticipantSetKey = TimeDealService.REDIS_PARTICIPANT_SET + eventId;
		return redisQueue.addParticipation(queueKey,  eventParticipantSetKey, clientPK) + 1;
    }

    @Transactional
    public int updateDeliveryEventCoupon(Long eventId, int participationsSize) {
        String lockKey = "EVENT:LOCK" + eventId;

        boolean isLockAcquired = redis.acquireLock(lockKey);

        if (!isLockAcquired) {
            throw new RedisLockAcquisitionException("Unable to acquire lock for event update.");
        }

        try {
            Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new InvalidEventException("Event not found for id: " + eventId));

            int excessParticipants = event.deliverCoupons(participationsSize);

            inMemoryEventRepository.save(event, ALIVE_MINUTE);
            eventRepository.save(event);

            return excessParticipants;
        } finally {
            redis.releaseLock(lockKey);
        }
    }


    public void validateEventParticipant(Long eventId, Map<String, Double> participations) {
        Iterator<Map.Entry<String, Double>> iterator = participations.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId, entry.getKey());

            if (byEventIdAndUserId.isPresent()) {
                log.debug("이미 참여한 유저입니다. 더 이상 쿠폰을 발급할 수 없습니다");
                iterator.remove();
            }
        }
    }

    public void bulkGenerateCoupon(Long eventId, List<String> clients) {
        Event event = inMemoryEventRepository.findById(eventId)
            .orElseThrow(() -> new InvalidEventException("Event not found in in-memory storage for id: " + eventId));


        clients.forEach(client -> {
            Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId, client);

            if (byEventIdAndUserId.isPresent()) {
                log.debug("The user has already participated. No additional coupons can be issued");
                return;
            }

            Coupon coupon = new Coupon(event, client);
            couponRepository.save(coupon);
        });
    }
}
