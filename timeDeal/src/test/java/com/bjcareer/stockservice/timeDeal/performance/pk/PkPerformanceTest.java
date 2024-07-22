package com.bjcareer.stockservice.timeDeal.performance.pk;


import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
public class PkPerformanceTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager em;

    private TimeDealEvent timeDealEvent;
    private static final int NUM_THREADS = 100;

    @BeforeEach
    @Commit
    void setUp() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        timeDealEvent = new TimeDealEvent(10);
        em.persist(timeDealEvent);
        em.getTransaction().commit();
    }

    @Test
    @Commit
    public void 싱글쓰레드로_ksuid_create_성능테스트() {
        //1.7초
    }

    @Test
    public void 싱글쓰레드로_search_성능테스트() {
        //500번째 찾는 것 277ms
        KusidCouponEntity kusidCouponEntity = new KusidCouponEntity(10.0, timeDealEvent);
        em.find(KusidCouponEntity.class, "2jb5xNSvfKXOUgScOUvtFjR5yq8");
    }


    @Test
    @Commit
    public void 싱글쓰레드로_seq_create_성능테스트() {
        //1.6초
        for(int i=0; i<10000; i++) {
            SeqCouponEntity seqCouponEntity = new SeqCouponEntity(10.0, timeDealEvent);
            em.persist(seqCouponEntity);
        }
    }

    @Test
    public void 싱글쓰레드로_seq_search_성능테스트() {
        //500번째 찾는 것 278ms
        SeqCouponEntity seqCouponEntity = new SeqCouponEntity(10.0, timeDealEvent);
        em.find(SeqCouponEntity.class, 500);
    }

    @Test
    @Commit
    public void 멀티쓰레드로_ksuid_create_성능테스트() throws InterruptedException {
        //1.397
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(() -> {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();

                for (int j = 0; j < 10; j++) {
                    KusidCouponEntity kusidCouponEntity = new KusidCouponEntity(10.0, timeDealEvent);
                    entityManager.persist(kusidCouponEntity);
                }

                entityManager.getTransaction().commit();
                entityManager.close();
            });

            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    @Commit
    public void 멀티쓰레드로_seq_create_성능테스트() throws InterruptedException {
        //1.397
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(() -> {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();

                for (int j = 0; j < 10; j++) {
                    SeqCouponEntity seqCouponEntity = new SeqCouponEntity(10.0, timeDealEvent);
                    entityManager.persist(seqCouponEntity);
                }

                entityManager.getTransaction().commit();
                entityManager.close();
            });

            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

}
