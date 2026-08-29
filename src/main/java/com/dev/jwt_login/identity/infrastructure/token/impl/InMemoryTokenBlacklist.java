package com.dev.jwt_login.identity.infrastructure.token.impl;

import com.dev.jwt_login.identity.infrastructure.token.TokenBlacklist;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenBlacklist implements TokenBlacklist {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public void blacklist(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
