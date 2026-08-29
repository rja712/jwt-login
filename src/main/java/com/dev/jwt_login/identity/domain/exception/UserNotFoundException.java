package com.dev.jwt_login.identity.domain.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
