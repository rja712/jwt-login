package com.dev.jwt_login.identity.domain.model.valueobject;

public record Username(String value) {

    public Username {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (value.length() < 3 || value.length() > 100) {
            throw new IllegalArgumentException("Username must be between 3 and 100 characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
