package com.keita.riggs.permission;

public enum UserPermission {

    RED("READ"),
    WRITE("WRITE"),
    DELETE("DELETE"),
    PUT("PUT"),
    UPDATE("UPDATE"),
    CREATE("POST");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
