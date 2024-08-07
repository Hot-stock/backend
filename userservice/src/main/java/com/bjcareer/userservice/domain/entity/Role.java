package com.bjcareer.userservice.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {

    @Id
    @GeneratedValue
    @Column(name="role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoleAssignments> assignments;

    public Role(RoleType type) {
        this.type = type;
        this.description = type.getDesc();
    }
}


