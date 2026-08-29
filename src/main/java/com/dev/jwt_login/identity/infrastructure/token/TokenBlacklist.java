package com.dev.jwt_login.identity.infrastructure.token;

public interface TokenBlacklist {

    void blacklist(String token);

    boolean isBlacklisted(String token);
}
