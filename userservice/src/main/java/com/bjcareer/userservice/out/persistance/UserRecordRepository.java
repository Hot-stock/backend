package com.bjcareer.userservice.out.persistance;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bjcareer.userservice.application.ports.out.PersistActiveUserPort;
import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.domain.entity.UserActive;
import com.bjcareer.userservice.out.persistance.repository.ActiveUserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRecordRepository implements PersistActiveUserPort {
	private final ActiveUserRepository repository;

	@Override
	public void persistActiveUser(UserActive userActive) {
		repository.save(userActive);
	}

	@Override
	public Optional<UserActive> findUserByLocaleDate(User user, LocalDate localDate) {
		return repository.findByUserInLocalDate(user, localDate);
	}
}
