package com.dev.jwt_login.identity.domain.exception;

public class DuplicateUsernameException extends DomainException {

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
}
