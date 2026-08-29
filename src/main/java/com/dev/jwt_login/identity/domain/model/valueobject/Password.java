package com.dev.jwt_login.identity.domain.model.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@EqualsAndHashCode
public final class Password {

    private final String encodedValue;

    private Password(String encodedValue) {
        this.encodedValue = encodedValue;
    }

    public static Password fromRaw(String rawPassword, PasswordEncoder encoder) {
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        return new Password(encoder.encode(rawPassword));
    }

    public static Password fromEncoded(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("Encoded password is required");
        }
        return new Password(encodedPassword);
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, encodedValue);
    }
}
