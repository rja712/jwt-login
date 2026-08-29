package com.dev.jwt_login.identity.infrastructure.token;

public interface TokenValidator {

    TokenClaims validate(String token);
}
