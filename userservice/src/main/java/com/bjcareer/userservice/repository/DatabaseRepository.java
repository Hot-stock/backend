package com.bjcareer.userservice.repository;

import com.bjcareer.userservice.domain.User;
import com.bjcareer.userservice.exceptions.DatabaseOperationException;
import com.bjcareer.userservice.exceptions.UserAlreadyExistsException;
import com.bjcareer.userservice.repository.queryConst.DatabaseQuery;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DatabaseRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(User user) {
        try {
            em.persist(user);
        } catch (EntityExistsException e) {
            handleException(e, "User already exists: {}", user.getId(),
                new UserAlreadyExistsException("User already exists with ID: " + user.getId()));
        } catch (PersistenceException e) {
            handleException(e, "PersistenceException during user save", e.getMessage(),
                new DatabaseOperationException("Error saving user to database", e));
        } catch (Exception e) {
            handleException(e, "Unexpected error during user save", e.getMessage(),
                new DatabaseOperationException("Unexpected error during user save", e));
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
            handleException(e, "PersistenceException during user retrieval", e.getMessage(),
                new DatabaseOperationException("Error retrieving user from database", e));
        } catch (Exception e) {
            handleException(e, "Unexpected error during user retrieval", e.getMessage(),
                new DatabaseOperationException("Unexpected error during user retrieval", e));
        }
        return Optional.empty();
    }

    private void handleException(Exception e, String logMessage, String detailMessage, RuntimeException exToThrow) {
        log.error(logMessage, detailMessage);
        throw exToThrow;
    }
}
