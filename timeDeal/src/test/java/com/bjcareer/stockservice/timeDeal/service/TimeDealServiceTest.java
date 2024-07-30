package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAsync
class TimeDealServiceTest {
    @Autowired private TimeDealService timeDealService;
    @Autowired private EventRepository timeDealEventRepository;
    @Autowired private InMemoryEventRepository inMemoryEventRepository;
    @Autowired EntityManagerFactory entityManagerFactory;
    @Autowired EntityManager em;

    TimeDealEvent timeDealEvent;

    @BeforeEach
    @Commit
    @Transactional
    void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        timeDealEvent = new TimeDealEvent(1);
        entityManager.persist(timeDealEvent);
        transaction.commit();
    }

    @Test
    void 이벤트_생성_요청(){
        TimeDealEvent timeDealEvent1 = timeDealService.createTimeDealEvent(100);
        assertEquals(timeDealEvent1.getPublishedCouponNum(), 100);
        assertEquals(timeDealEvent1.getDeliveredCouponNum(), 0);
    }

    @Test
    void 잘못된_이벤트Id로_쿠폰_발급을_요청하는_경우(){
        assertThrows(IllegalStateException.class , () -> timeDealService.generateCouponToUser(-99L, 20.0));
    }

    @Test
    void 티켓을_더이상_발급할_수_없는_상황일(){
        TimeDealEvent localTimeDealEvent = new TimeDealEvent(0);
        timeDealEventRepository.save(localTimeDealEvent);

        assertThrows(IllegalStateException.class , () -> timeDealService.generateCouponToUser(localTimeDealEvent.getId(), 20.0));
    }

    @Test
    void 사람들이_쿠폰보다_더_많은_수를_요청하는_경우() throws InterruptedException {
        Long saveId = inMemoryEventRepository.save(timeDealEvent);

        Runnable runnable = () -> {
            timeDealService.generateCouponToUser(saveId, 20.0); //em공유가 안된다는 점
        };

        Runnable runnable1 = () -> {
            try {
                timeDealService.generateCouponToUser(saveId, 20.0); //em공유가 안된다는 점
            }catch (Exception e){

            }

        };

        Thread thread = new Thread(runnable, "User A");
        Thread thread1 = new Thread(runnable1, "User B");

        thread.start();
        thread1.start();

        thread.join();
        thread1.join();

        Thread.sleep(500);

        TimeDealEvent event = timeDealEventRepository.findById(timeDealEvent.getId());
        System.out.println("event = " + event.getDeliveredCouponNum());

        assertEquals(event.getDeliveredCouponNum(), 1);
    }


    @Test
    @Transactional
    void 사람들이_쿠폰보다_더_많은_수를_요청하는_경우_no_thread() throws InterruptedException {
        timeDealService.generateCouponToUser(timeDealEvent.getId(), 20.0); //em공유가 안된다는 점
        timeDealService.generateCouponToUser(timeDealEvent.getId(), 20.0); //em공유가 안된다는 점


        Thread.sleep(500);

        assertEquals(1, timeDealEvent.getDeliveredCouponNum());
    }

}