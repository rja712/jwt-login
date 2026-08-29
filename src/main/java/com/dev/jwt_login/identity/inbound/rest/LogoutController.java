package com.dev.jwt_login.identity.inbound.rest;

import com.dev.jwt_login.identity.infrastructure.token.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final TokenBlacklist tokenBlacklist;

    @PostMapping
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklist(token);
        }

        return ResponseEntity.ok().build();
    }
}
