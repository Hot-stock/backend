package com.bjcareer.userservice.domain.entity;

import java.io.Serializable;

public enum RoleType {
    ALL("All User asscess"),
    ADMIN("Administrator with full access"),
    USER("Regular user with limited access"),
    DEVELOPER("Developer with access to development tools");

    private final String desc;

    RoleType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
