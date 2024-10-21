package com.bjcareer.stockservice.timeDeal.application.ports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.application.ports.in.AddParticipantCommand;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CouponUsecase;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CreateEventCommand;
import com.bjcareer.stockservice.timeDeal.application.ports.in.CreateEventUsecase;
import com.bjcareer.stockservice.timeDeal.application.ports.out.LoadUserPort;
import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;
import com.bjcareer.stockservice.timeDeal.out.api.authServer.UserResponseDTO;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TimeDealService implements CreateEventUsecase, CouponUsecase {
    public static final String REDIS_QUEUE_NAME = "EVENT:QUEUE:";
    public static final String REDIS_PARTICIPANT_SET = "EVENT:PARTICIPANT:";
    private static final long ALIVE_MINUTE = 5L;

    private final LoadUserPort loadUserPort;

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final RedisQueue redisQueue;
    private final Redis redis;

    @Override
    @Transactional
    public Event createEvent(CreateEventCommand command) {
        Event event = new Event(command.getPublishedCouponNum(), command.getDiscountRate());
        return eventRepository.save(event);
    }

    @Override
    public int addParticipation(AddParticipantCommand command) {
        String queueKey = TimeDealService.REDIS_QUEUE_NAME + command.getEventId();
        String eventParticipantSetKey = TimeDealService.REDIS_PARTICIPANT_SET + command.getEventId();

        //port to AuthServerforGetUserId
        UserResponseDTO userResponseDTO = loadUserPort.loadUserUsingSessionId(command.getSessionId());
        log.info("User {} request coupon {}", command.getEventId(), userResponseDTO.getId());

        ParticipationDomain participation = new ParticipationDomain(command.getEventId().toString(),
            command.getSessionId());
        return redisQueue.addParticipation(queueKey, eventParticipantSetKey, participation) + 1;
    }

    @Transactional
    public void generateCouponUsecase(AddParticipantCommand command) {
        Long eventId = command.getEventId();
        String sessionId = command.getSessionId();

        Event event = inMemoryEventRepository.findById(eventId)
            .orElseThrow(() -> new InvalidEventException("Event not found in in-memory storage for id: " + eventId));

        Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId, sessionId);

        if (byEventIdAndUserId.isPresent()) {
            log.debug("The user has already participated. No additional coupons can be issued");
            return;
        }

        Coupon coupon = new Coupon(event, sessionId);
        couponRepository.save(coupon);
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

    public void validateEventParticipant(Long eventId, Map<String, ParticipationDomain> participations) {
        Iterator<Map.Entry<String, ParticipationDomain>> iterator = participations.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ParticipationDomain> entry = iterator.next();
            Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId, entry.getKey());

            if (byEventIdAndUserId.isPresent()) {
                log.debug("이미 참여한 유저입니다. 더 이상 쿠폰을 발급할 수 없습니다");
                iterator.remove();
            }
        }
    }

    @Transactional
    public void bulkGenerateCoupon(Long eventId, List<ParticipationDomain> clients) {
        List<Coupon> coupons = new ArrayList<>();
        Event event = inMemoryEventRepository.findById(eventId)
            .orElseThrow(() -> new InvalidEventException("Event not found in in-memory storage for id: " + eventId));


        clients.forEach(client -> {
            Optional<Coupon> byEventIdAndUserId = couponRepository.findByEventIdAndUserId(eventId,
                client.getClientId());

            if (byEventIdAndUserId.isPresent()) {
                client.setResult(false);
                log.debug("The user has already participated. No additional coupons can be issued");
                return;
            }

            client.setResult(true);
            coupons.add(new Coupon(event, client.getClientId()));
        });

        couponRepository.saveAll(coupons);
    }
}
