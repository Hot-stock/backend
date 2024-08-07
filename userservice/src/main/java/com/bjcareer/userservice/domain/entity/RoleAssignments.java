package com.bjcareer.userservice.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoleAssignments {
    @Id @GeneratedValue
    @Column(name = "role_assignment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public RoleAssignments(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
