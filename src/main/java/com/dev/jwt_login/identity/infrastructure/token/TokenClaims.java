package com.dev.jwt_login.identity.infrastructure.token;

import java.util.List;

public record TokenClaims(String username, List<String> roles) {
}
