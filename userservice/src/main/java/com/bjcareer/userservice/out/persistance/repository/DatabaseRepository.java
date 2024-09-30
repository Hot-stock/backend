package com.bjcareer.userservice.out.persistance.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bjcareer.userservice.domain.entity.User;

public interface DatabaseRepository extends CrudRepository<User, Long> {
	Optional<User> findByemail(String email);
}
