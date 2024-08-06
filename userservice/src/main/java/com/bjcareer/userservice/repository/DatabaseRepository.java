package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.domain.User;
import com.bjcareer.userservice.exceptions.DatabaseOperationException;
import com.bjcareer.userservice.exceptions.UserAlreadyExistsException;
import com.bjcareer.userservice.exceptions.UserNotFoundException;
import com.bjcareer.userservice.repository.queryConst.DatabaseQuery;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Slf4j
public class DatabaseRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(User user) {
        try {
            em.persist(user);
        } catch (EntityExistsException e) {
            log.error("User already exists: {}", user.getId());
            throw new UserAlreadyExistsException("User already exists with ID: " + user.getId());
        } catch (PersistenceException e) {
            log.error("PersistenceException during user save: {}", e.getMessage());
            throw new DatabaseOperationException("Error saving user to database", e);
        } catch (Exception e) {
            log.error("Unexpected error during user save: {}", e.getMessage());
            throw new DatabaseOperationException("Unexpected error during user save", e);
        }
    }

    public Optional<User> findByUserId(String userId) {
        try {
            TypedQuery<User> query = em.createQuery(DatabaseQuery.finedUsertQuery, User.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            log.warn("No user found with userId: {}", userId);
            return Optional.empty();
        } catch (PersistenceException e) {
            log.error("PersistenceException during user retrieval: {}", e.getMessage());
            throw new DatabaseOperationException("Error retrieving user from database", e);
        } catch (Exception e) {
            log.error("Unexpected error during user retrieval: {}", e.getMessage());
            throw new DatabaseOperationException("Unexpected error during user retrieval", e);
        }
    }
}
