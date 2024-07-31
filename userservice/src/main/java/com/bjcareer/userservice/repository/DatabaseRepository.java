package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DatabaseRepository {
    private final EntityManager em;

    public boolean save(User user) {
        try {
            em.persist(user);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public Optional<User> findByUserId(String userId) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.userId = :userId", User.class);
        query.setParameter("userId", userId);

        try{
            User singleResult = query.getSingleResult();
            return Optional.of(singleResult);
        }catch (NoResultException e){
            log.debug(e.getMessage(), "찾고자 하는 사용자 없음");
            return Optional.empty();
        }
    }
}
