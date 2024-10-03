package com.bjcareer.userservice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "user_active",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "lastLogin"})
	}
)
public class UserActive {
	@Id
	@GeneratedValue
	private Long id;
	private LocalDate lastLogin;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public UserActive(User user) {
		this.user = user;
		lastLogin = LocalDateTime.now().toLocalDate();
	}
}
