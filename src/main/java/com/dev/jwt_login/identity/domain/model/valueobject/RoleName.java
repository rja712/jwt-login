package com.dev.jwt_login.identity.domain.model.valueobject;

public record RoleName(String value) {

    public RoleName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
