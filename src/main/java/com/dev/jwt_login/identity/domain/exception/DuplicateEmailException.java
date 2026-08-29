package com.dev.jwt_login.identity.domain.exception;

public class DuplicateEmailException extends DomainException {

    public DuplicateEmailException(String email) {
        super("Email already exists: " + email);
    }
}
