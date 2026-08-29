package com.dev.jwt_login.identity.domain.model.entity;

import com.dev.jwt_login.identity.domain.model.valueobject.RoleName;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record Role(Long id, RoleName name, Set<Permission> permissions) {

    public Role(Long id, RoleName name, Set<Permission> permissions) {
        if (name == null) {
            throw new IllegalArgumentException("Role name is required");
        }
        this.id = id;
        this.name = name;
        this.permissions = permissions == null ? new HashSet<>() : new HashSet<>(permissions);
    }

    @Override
    public Set<Permission> permissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
