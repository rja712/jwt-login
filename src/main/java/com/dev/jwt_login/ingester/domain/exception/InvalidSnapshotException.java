package com.dev.jwt_login.ingester.domain.exception;

public class InvalidSnapshotException extends IngestionException {

    public InvalidSnapshotException(String message) {
        super(message);
    }
}
