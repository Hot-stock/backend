package com.bjcareer.stockservice.timeDeal.repository;

import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;

public interface InMemoryEventRepository {
    public static final String EVENT_BUCKET = "EVENT:";
    public static final String COUPON_BUCKET = "COUPON:";
    public static final String BACKUP = ":BACKUP";

    public void saveCoupon(Coupon coupon, Long aliveMinute);
    public Long save(Event timeDealEvent, Long aliveMinute);
    public Optional<Event> findById(Long id);
    public <V> Optional<V> findBackupObject(String key, Class<V> type);
    public void deleteKey(String key);
    public void saveClient(Event timeDealEvent, Long aliveMinute, String clientPK);
    public Optional<String> findParticipant(Event timeDealEvent, String clientPK);
}
