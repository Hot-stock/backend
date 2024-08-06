package com.bjcareer.stockservice.timeDeal.repository.event;


import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public class RedisTimeDealRepository implements InMemoryEventRepository {
    private final RedissonClient redissonClient;
    private final String BUCKET = "timeDeal:";

    @Override
    public Long save(TimeDealEvent timeDealEvent) {
        String BUCKET_NAME = BUCKET + timeDealEvent.getId();
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket(BUCKET_NAME);
        bucket.set(timeDealEvent);
        return timeDealEvent.getId();
    }

    @Override
    public TimeDealEvent findById(Long id) {
        String BUCKET_NAME = BUCKET + id;
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket(BUCKET_NAME);
        return bucket.get();
    }
}
