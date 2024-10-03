package com.bjcareer.userservice.out.persistance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bjcareer.userservice.domain.entity.User;
import com.bjcareer.userservice.domain.entity.UserActive;

public interface ActiveUserRepository extends CrudRepository<UserActive, Long> {
	@Query("SELECT u FROM UserActive u WHERE u.user = :user AND u.lastLogin = :localDate")
	Optional<UserActive> findByUserInLocalDate(@Param("user") User user, @Param("localDate") LocalDate localDate);

	@Query("SELECT u FROM UserActive u WHERE u.lastLogin = :localDate")
	List<UserActive> findDailyActiveUsers(@Param("localDate") LocalDate localDate);
}
