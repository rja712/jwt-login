package com.dev.jwt_login.identity.inbound.model.response;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<String> roles
) {
}
