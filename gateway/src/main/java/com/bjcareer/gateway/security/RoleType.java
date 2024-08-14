package com.bjcareer.gateway.security;

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
