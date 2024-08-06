package com.bjcareer.userservice.domain;

import com.github.ksuid.KsuidGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
public class User {

    @Id
    private String id = KsuidGenerator.generate();

    @Column(unique=true, name = "user_id")
    private String userId;

    private String password;

    @Column(name = "telegram_id")
    private String telegramId;

    @Version
    private Long version;

    public User(String userId, String password, String telegramId) {
        this.userId = userId;
        this.password = password;
        this.telegramId = telegramId;
    }

    public boolean verifyPassword(String password){
        if (this.password.equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", telegramId='" + telegramId + '\'' +
                ", version=" + version +
                '}';
    }
}