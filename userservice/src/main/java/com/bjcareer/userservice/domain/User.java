package com.bjcareer.userservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    String id;

    @Column(unique=true, name = "user_id")
    String userId;
    String password;

    @Column(name = "telegram_id")
    String telegramId;
}
