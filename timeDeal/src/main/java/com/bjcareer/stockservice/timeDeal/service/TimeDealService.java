package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.RedisLock;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerErrorException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    private final CouponRepository couponRepository;
    private final EventRepository timeDealEventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final RedisLock redisLock;
    private final String LOCK_KEK = "TIME_DEAL_LOCK";

    @Transactional
    public Optional<TimeDealEvent> createTimeDealEvent(int publishedCouponNum) {
        log.debug("타임딜 서비스 시작");

        TimeDealEvent timeDealEvent = new TimeDealEvent(publishedCouponNum);
        boolean isLocked = redisLock.tryLock(LOCK_KEK);

        if (!isLocked){
            return Optional.empty();
        }

        Long saveId = timeDealEventRepository.save(timeDealEvent);
        inMemoryEventRepository.save(timeDealEvent);

        redisLock.releaselock(LOCK_KEK);
        log.debug("저장된 ID는 {}", saveId);

        return Optional.of(timeDealEvent);
    }

    public Optional<Coupon> generateCouponToUser(Long eventId, Double discountRate) {
        boolean isValidEventId = validateEventId(eventId);

        if (!isValidEventId){
            throw new IllegalStateException("Invalid Event ID: " + eventId);
        }

        boolean isLocked = redisLock.tryLock(LOCK_KEK);

        if (!isLocked) {
            throw new RedisLockAcquisitionException("Can't Get Lock Try");
        }

        TimeDealEvent timeDealEvent = inMemoryEventRepository.findById(eventId);
        boolean isDelivered = timeDealEvent.incrementDeliveredCouponIfPossible();
        Coupon result;

        if (isDelivered) {
            inMemoryEventRepository.save(timeDealEvent);
            result = generateCouponAndSaveToDatabase(discountRate, timeDealEvent);
        }else{
            timeDealEventRepository.saveAsync(timeDealEvent);
            result = null;
        }

        redisLock.releaselock(LOCK_KEK);
        return Optional.ofNullable(result);
    }

    protected Coupon generateCouponAndSaveToDatabase(Double discountRate, TimeDealEvent timeDealEvent) {
        Coupon coupon = new Coupon(discountRate, timeDealEvent);
        couponRepository.saveAsync(coupon);
        return coupon;
    }

    private boolean validateEventId(Long eventId) {
        TimeDealEvent byId = inMemoryEventRepository.findById(eventId);
        return byId != null;
    }
}
