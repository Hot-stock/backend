package com.bjcareer.stockservice.timeDeal.repository;

import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;

public interface InMemoryEventRepository {
    String EVENT_BUCKET = "EVENT:";
    String COUPON_BUCKET = "COUPON:";
    String BACKUP = ":BACKUP";

    void saveCoupon(Coupon coupon, Long aliveMinute);

    Long save(Event timeDealEvent, Long aliveMinute);

    Optional<Event> findById(Long id);

    <V> Optional<V> findBackupObject(String key, Class<V> type);

    void deleteKey(String key);

    void saveClient(Event timeDealEvent, Long aliveMinute, String clientPK);

    Optional<String> findParticipant(Event timeDealEvent, String clientPK);
}
