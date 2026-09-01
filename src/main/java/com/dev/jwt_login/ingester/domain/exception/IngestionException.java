package com.dev.jwt_login.ingester.domain.exception;

public abstract class IngestionException extends RuntimeException {

    protected IngestionException(String message) {
        super(message);
    }
}
