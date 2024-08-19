package com.bjcareer.stockservice.timeDeal.repository.event;


import java.time.Duration;
import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public class RedisTimeDealRepository implements InMemoryEventRepository {
    private final RedissonClient redissonClient;
    public static final String BUCKET = "EVENT:";

    @Override
    public Long save(Event timeDealEvent, Long aliveMinute) {
        String BUCKET_NAME = BUCKET + timeDealEvent.getId();
        RBucket<Event> bucket = redissonClient.getBucket(BUCKET_NAME);
        bucket.set(timeDealEvent, Duration.ofMinutes(aliveMinute));  // TTL(Duration)을 설정하여 데이터의 유효 기간을 설정
        return timeDealEvent.getId();
    }

    @Override
    public Optional<Event> findById(Long id) {
        String BUCKET_NAME = BUCKET + id;
        RBucket<Event> bucket = redissonClient.getBucket(BUCKET_NAME);
        return Optional.ofNullable(bucket.get());  // NullPointerException 방지를 위해 Optional.ofNullable() 사용
    }

    @Override
    public void saveClient(Event timeDealEvent, Long aliveMinute, String clientPK) {
        String BUCKET_NAME = BUCKET + timeDealEvent.getId() + ":" + clientPK;
        RBucket<String> bucket = redissonClient.getBucket(BUCKET_NAME);
        bucket.set(clientPK, Duration.ofMinutes(aliveMinute));
    }

    @Override
    public Optional<String> findParticipant(Event timeDealEvent, String clientPK) {
        String BUCKET_NAME = BUCKET + timeDealEvent.getId() + ":" + clientPK;
        RBucket<String> bucket = redissonClient.getBucket(BUCKET_NAME);
        return Optional.ofNullable(bucket.get());
    }
}
