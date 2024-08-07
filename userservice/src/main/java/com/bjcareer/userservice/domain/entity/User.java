package com.bjcareer.userservice.domain.entity;

import com.github.ksuid.KsuidGenerator;
import com.google.common.hash.Hashing;
import com.google.common.base.Charsets;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id @Column(name = "user_id")
    private String id = KsuidGenerator.generate();

    @Column(unique=true)
    private String alais;
    private String password;

    @Column(name = "telegram_id")
    private String telegramId;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoleAssignments> assignments = new ArrayList<>();

    public User(String userId, String password, String telegramId) {
        this.alais = userId;
        this.password = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        this.telegramId = telegramId;
        this.userType = UserType.NORMAL;
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
                ", userId='" + alais + '\'' +
                ", telegramId='" + telegramId + '\'' +
                ", version=" + version +
                '}';
    }
}