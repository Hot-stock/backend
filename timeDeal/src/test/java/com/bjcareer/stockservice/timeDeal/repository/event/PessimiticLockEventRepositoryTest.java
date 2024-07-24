package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @Transactional
@EnableAsync
class PessimiticLockEventRepositoryTest {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void 비동기_이벤트_저장() throws InterruptedException {
        TimeDealEvent timeDealEvent = new TimeDealEvent(100);
        eventRepository.saveAsync(timeDealEvent);
        Thread.sleep(500);
        assertNotNull(timeDealEvent.getId());
    }
}