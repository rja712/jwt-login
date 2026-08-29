package com.dev.jwt_login.identity.domain.exception;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
