package com.dev.jwt_login.identity.domain.model.valueobject;

public record UserId(Long value) {

    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("User id is required");
        }
    }
}
