package com.bjcareer.userservice.application.ports.out.message;

import com.bjcareer.userservice.domain.entity.User;

import lombok.Getter;

@Getter
public class UserLoggedInRecorderEvent {
	private User user;

	public UserLoggedInRecorderEvent(User user) {
		this.user = user;
	}
}
