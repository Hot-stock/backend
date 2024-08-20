package com.bjcareer.stockservice.timeDeal.repository.event;


import java.time.Duration;
import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public class RedisTimeDealRepository implements InMemoryEventRepository {
    private final RedissonClient redissonClient;


    @Override
    public Long save(Event timeDealEvent, Long aliveMinute) {
        String BUCKET_NAME = EVENT_BUCKET +  timeDealEvent.getId();

        RBucket<Event> bucket = redissonClient.getBucket(BUCKET_NAME);
        RBucket<Event> backupBucket = redissonClient.getBucket(BUCKET_NAME + BACKUP);

        bucket.set(timeDealEvent, Duration.ofMinutes(aliveMinute));  // TTL(Duration)을 설정하여 데이터의 유효 기간을 설정
        backupBucket.set(timeDealEvent);

        return timeDealEvent.getId();
    }

    @Override
    public void saveCoupon(Coupon coupon, Long aliveMinute) {
        String BUCKET_NAME = COUPON_BUCKET +  coupon.getId();

        RBucket<Coupon> bucket = redissonClient.getBucket(BUCKET_NAME);
        RBucket<Coupon> backupBucket = redissonClient.getBucket(BUCKET_NAME + BACKUP);

        bucket.set(coupon, Duration.ofMinutes(aliveMinute));  // TTL(Duration)을 설정하여 데이터의 유효 기간을 설정
        backupBucket.set(coupon);
    }

    @Override
    public <V> Optional<V> findBackupObject(String key, Class<V> type) {
        RBucket<V> bucket = redissonClient.getBucket(key);
        V v = bucket.get();

        // Check if the retrieved object is of the expected type
        if (type.isInstance(v)) {
            return Optional.ofNullable(v);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteKey(String key) {
        RBucket<Event> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    @Override
    public Optional<Event> findById(Long id) {
        String BUCKET_NAME = EVENT_BUCKET + id;
        RBucket<Event> bucket = redissonClient.getBucket(BUCKET_NAME);
        return Optional.ofNullable(bucket.get());
    }

    @Override
    public void saveClient(Event timeDealEvent, Long aliveMinute, String clientPK) {
        String BUCKET_NAME = EVENT_BUCKET + timeDealEvent.getId() + ":" + clientPK;
        RBucket<String> bucket = redissonClient.getBucket(BUCKET_NAME);
        bucket.set(clientPK, Duration.ofMinutes(aliveMinute));
    }

    @Override
    public Optional<String> findParticipant(Event timeDealEvent, String clientPK) {
        String BUCKET_NAME = EVENT_BUCKET + timeDealEvent.getId() + ":" + clientPK;
        RBucket<String> bucket = redissonClient.getBucket(BUCKET_NAME);
        return Optional.ofNullable(bucket.get());
    }
}