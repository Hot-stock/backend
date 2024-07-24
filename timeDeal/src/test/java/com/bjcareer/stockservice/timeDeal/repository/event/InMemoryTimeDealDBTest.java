package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class InMemoryTimeDealDBTest {
    @Autowired private EventRepository eventRepository;
    @Autowired private RedissonClient  redissonClient;
    @Autowired private InMemoryEventRepository inMemoryTimeDealDB;
    TimeDealEvent timeDealEvent;


    @BeforeEach
    void setUp() {
        timeDealEvent = new TimeDealEvent(100);
        eventRepository.save(timeDealEvent);
    }

    @Test
    void 테스트_연결_설정(){
        String id = redissonClient.getId();
        System.out.println("id = " + id);
    }

    @Test
    void event_티켓_저장(){
        int TICKET_COUNT = 100;
        TimeDealEvent event = new TimeDealEvent(TICKET_COUNT);
        Long save = eventRepository.save(event);

        RMap<Long, TimeDealEvent> test = redissonClient.getMap("TimeDealEvet" + save.toString());
        test.put(event.getId(), event);
        TimeDealEvent event1 = test.get(event.getId());

        assertEquals(event.getPublishedCouponNum(), event1.getPublishedCouponNum());
    }

    @Test
    void event가_저장되는지_테스트(){
        Long save = inMemoryTimeDealDB.save(timeDealEvent);
        RBucket<TimeDealEvent> bucket = redissonClient.getBucket("timeDeal:" + save.toString());
        assertEquals(save, bucket.get().getId());
    }


    @Test
    void event가_찾아지는지(){
        Long save = inMemoryTimeDealDB.save(timeDealEvent);
        TimeDealEvent event = inMemoryTimeDealDB.findById(save);
        assertEquals(timeDealEvent.getId(), event.getId());
    }


    @Test
    void 레디스_동시성_이슈_테스트() throws InterruptedException {
        int TICKET_COUNT = 100;
        int USER_COUNt = 2;

        TimeDealEvent event = new TimeDealEvent(TICKET_COUNT);
        Long save = eventRepository.save(event);
        // 키 설정 이슈
        RMap<Long, TimeDealEvent> test = redissonClient.getMap("TimeDealEvet:" + save.toString());
        test.put(event.getId(), event);
        RLock lock = redissonClient.getLock("TimeDealEvet:1" + save.toString());

        Thread user1 = new Thread(() -> {
            try {
                System.out.println("\"락획득한다\" = " + "락획득한다");
                boolean b = lock.tryLock(1, 1, TimeUnit.MINUTES);
                System.out.println("\"락실패\" = " + "락실패");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            TimeDealEvent event1 = test.get(event.getId());
            event1.updateDeliveredCouponNum();
            test.put(event.getId(), event1);

            lock.unlock();
        });

        Thread user2 = new Thread(() -> {

            try {

                System.out.println("\"락획득한다\" = " + "락획득한다");
                boolean b = lock.tryLock(1, 1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            TimeDealEvent event1 = test.get(event.getId());
            event1.updateDeliveredCouponNum();
            test.put(event.getId(), event1);

            lock.unlock();
        });

        user1.start();
        user2.start();
        user1.join();
        user2.join();

        TimeDealEvent result = test.get(event.getId());
        System.out.println("result = " + result.hashCode());
        System.out.println("event = " + event.hashCode());
        assertEquals(USER_COUNt, result.getDeliveredCouponNum());
    }
}