package com.dev.jwt_login.identity.domain.model.valueobject;

import org.apache.commons.validator.routines.EmailValidator;

public record Email(String value) {

    private static final EmailValidator VALIDATOR = EmailValidator.getInstance();

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!VALIDATOR.isValid(value)) {
            throw new IllegalArgumentException("Email must be valid");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
