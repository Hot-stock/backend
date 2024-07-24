package com.bjcareer.stockservice.timeDeal.repository.event;


import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public class RedisTimeDealRepository implements InMemoryEventRepository {
    private final RedissonClient redissonClient;

    @Override
    public Long save(TimeDealEvent timeDealEvent) {
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket("timeDeal:" + timeDealEvent.getId());
        bucket.set(timeDealEvent);
        return timeDealEvent.getId();
    }

    @Override
    public TimeDealEvent findById(Long id) {
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket("timeDeal:" + id);
        return bucket.get();
    }
}
