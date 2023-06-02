package com.keita.riggs.permission;

public enum UserPermission {

    USER_RED("READ"),
    USER_WRITE("WRITE"),
    USER_DELETE("DELETE"),
    USER_PUT("PUT"),
    USER_UPDATE("UPDATE"),
    USER_CREATE("POST");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
