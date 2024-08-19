package com.bjcareer.stockservice.timeDeal.repository;

import java.time.Duration;
import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;

public interface InMemoryEventRepository {
    public static final String BUCKET = "EVENT:";
    public static final String BACKUP = ":BACKUP";

    public Long save(Event timeDealEvent, Long aliveMinute);
    public Optional<Event> findById(Long id);
    public Optional<Event> findBackupObject(String key);
    public void deleteKey(String key);
    public void saveClient(Event timeDealEvent, Long aliveMinute, String clientPK);
    public Optional<String> findParticipant(Event timeDealEvent, String clientPK);
}
