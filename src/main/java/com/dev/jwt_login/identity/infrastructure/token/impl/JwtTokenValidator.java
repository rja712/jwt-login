package com.dev.jwt_login.identity.infrastructure.token.impl;

import com.dev.jwt_login.identity.infrastructure.token.TokenClaims;
import com.dev.jwt_login.identity.infrastructure.token.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenValidator implements TokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public TokenClaims validate(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = body.getSubject();
            List<?> rawRoles = body.get("roles", List.class);
            List<String> roles = rawRoles == null
                    ? Collections.emptyList()
                    : rawRoles.stream().map(Object::toString).collect(Collectors.toList());

            return new TokenClaims(username, roles);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
