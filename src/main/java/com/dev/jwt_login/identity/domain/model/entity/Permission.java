package com.dev.jwt_login.identity.domain.model.entity;

public record Permission(Long id, String name) {

    public Permission {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Permission name is required");
        }
    }
}
