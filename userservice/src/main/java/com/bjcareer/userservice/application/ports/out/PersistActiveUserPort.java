package com.bjcareer.userservice.application.ports.out;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.domain.entity.UserActive;

public interface PersistActiveUserPort {
	void persistActiveUser(UserActive userActive);
	Optional<UserActive> findUserByLocaleDate(User user, LocalDate localDate);
}
