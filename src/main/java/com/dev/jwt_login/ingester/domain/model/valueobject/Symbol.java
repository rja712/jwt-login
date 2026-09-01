package com.dev.jwt_login.ingester.domain.model.valueobject;

/**
 * The trading symbol a snapshot belongs to, e.g. "RELIANCE" or "NIFTY 50".
 */
public record Symbol(String value) {

    public Symbol {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Symbol is required");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Symbol must be at most 50 characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
