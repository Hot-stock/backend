package com.bjcareer.userservice.out.persistance;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bjcareer.userservice.application.ports.out.CreateUserPort;
import com.bjcareer.userservice.application.ports.out.LoadUserPort;
import com.bjcareer.userservice.domain.entity.Role;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.out.persistance.repository.DatabaseRepository;
import com.bjcareer.userservice.out.persistance.repository.exceptions.DatabaseOperationException;
import com.bjcareer.userservice.out.persistance.repository.queryConst.DatabaseQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PostgreDatabaseAdapter implements LoadUserPort, CreateUserPort {
	@PersistenceContext
	private EntityManager em;
	private final DatabaseRepository databaseRepository;

	@Override
	public void save(User user) {
		databaseRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return databaseRepository.findByemail(email);
	}

	public Optional<Role> findRoleByRoleType(RoleType roleType) {
		try {
			TypedQuery<Role> query = em.createQuery(DatabaseQuery.finedRoleQuery, Role.class);
			query.setParameter("type", roleType.name());
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			log.warn("No Role found with role name: {}", roleType);
			return Optional.empty();
		} catch (PersistenceException e) {
			log.error("PersistenceException during role retrieval: {}", e.getMessage());
			throw new DatabaseOperationException("Error retrieving user from database", e);
		} catch (Exception e) {
			log.error("Unexpected error during role retrieval: {}", e.getMessage());
			throw new DatabaseOperationException("Unexpected error during user retrieval", e);
		}
	}
}
