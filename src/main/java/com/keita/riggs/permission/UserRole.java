package com.keita.riggs.permission;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.keita.riggs.permission.UserPermission.*;

public enum UserRole {

    ADMIN(Sets.newHashSet(RED, WRITE, CREATE, DELETE, PUT, UPDATE)),
    USER(Sets.newHashSet(RED, WRITE));

    private final Set<UserPermission> userPermissions;

    UserRole(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public Set<SimpleGrantedAuthority> grantedAuthorities() {
        Set<SimpleGrantedAuthority> permission =
                getUserPermissions()
                        .stream()
                        .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                        .collect(Collectors.toSet());
        permission.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permission;
    }

}
