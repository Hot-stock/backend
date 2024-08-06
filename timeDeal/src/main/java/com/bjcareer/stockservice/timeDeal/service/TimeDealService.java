package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.RedisLock;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    private final CouponRepository couponRepository;
    private final EventRepository timeDealEventRepository;
    private final InMemoryEventRepository inMemoryEventRepository;
    private final RedisLock redisLock;

    @Transactional
    public TimeDealEvent createTimeDealEvent(int publishedCouponNum) {
        log.debug("타임딜 서비스 시작");
        String lockName = "timeDealEventLock";
        TimeDealEvent timeDealEvent = new TimeDealEvent(publishedCouponNum);
        boolean isLocked = redisLock.tryLock(lockName);

        if (isLocked){
            Long saveId = timeDealEventRepository.save(timeDealEvent);
            inMemoryEventRepository.save(timeDealEvent);
            redisLock.releaselock(lockName);
            log.debug("저장된 ID는 {}", saveId);
        }else {
            throw new IllegalStateException("레디스 락 획득 실패");
        }

        return timeDealEvent;
    }

    public Coupon generateCouponToUser(Long eventId, Double discountRate) {
        log.info("요청된 이벤트 ID {}", eventId);
        vaildateEventId(eventId);

        String lockName = "generateCouponToUserLock";
        boolean is_locked = redisLock.tryLock(lockName);

        if (!is_locked) {
            throw new IllegalStateException("사용자 time-out");
        }

        log.info("lock 획득 {}", Thread.currentThread().getName());

        TimeDealEvent timeDealEvent = inMemoryEventRepository.findById(eventId);
        vaildateRemainCoupon(timeDealEvent);
        timeDealEvent.updateDeliveredCouponNum();

        log.info("사용자에게 전달된 쿠폰의 개수는 = {} ", timeDealEvent.getDeliveredCouponNum());

        inMemoryEventRepository.save(timeDealEvent);
        Coupon result = generateCoupon(discountRate, timeDealEvent);
        log.info("발급된 쿠폰 ID는 {}", result.getCouponNumber());
        redisLock.releaselock(lockName);

        return result;
    }

    protected Coupon generateCoupon(Double discountRate, TimeDealEvent timeDealEvent) {
        Coupon coupon = new Coupon(discountRate, timeDealEvent);
        couponRepository.saveAsync(coupon);
        return coupon;
    }

    private void vaildateRemainCoupon(TimeDealEvent timeDealEvent) {
        if(timeDealEvent.getPublishedCouponNum() <= timeDealEvent.getDeliveredCouponNum()){
            timeDealEventRepository.saveAsync(timeDealEvent);
            throw new IllegalStateException("더 이상 발급하지 못함");
        }
    }

    private void vaildateEventId(Long eventId) {
        TimeDealEvent byId = inMemoryEventRepository.findById(eventId);
        if (byId == null) {
            throw  new IllegalStateException("잘못된 Event를 요청함");
        }
    }
}
